package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.tm.tashman.config.jwt.JwtUtils;
import uz.tm.tashman.entity.Client;
import uz.tm.tashman.entity.Role;
import uz.tm.tashman.entity.User;
import uz.tm.tashman.entity.UserAgent;
import uz.tm.tashman.enums.ERole;
import uz.tm.tashman.enums.Gender;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.*;
import uz.tm.tashman.models.requestModels.AuthenticationRequestModel;
import uz.tm.tashman.models.requestModels.UserRequestModel;
import uz.tm.tashman.repository.RoleRepository;
import uz.tm.tashman.util.AES;
import uz.tm.tashman.util.HTTPUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static uz.tm.tashman.enums.StatusCodes.*;

@Service
public class ClientService extends HTTPUtil {

    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserAgentService userAgentService;
    private final LogService logService;
    final UserService userService;

    @Value("${local.file.path}")
    String filePath;

    public ClientService(RoleRepository roleRepository, JwtUtils jwtUtils, AuthenticationManager authenticationManager, LogService logService, UserAgentService userAgentService, UserService userService) {
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.logService = logService;
        this.userAgentService = userAgentService;
    }

    public Client createClient(UserModel userModel, User user) {
        Client client = new Client();
        client.setUser(user);
        client.setName(userModel.getName());
        client.setSurname(userModel.getSurname());
        client.setDob(userModel.getDob());
        client.setGender(Gender.getByCode(userModel.getGender()));
        client.setCreatedDate(user.getCreatedDate());
        return client;
    }

    public UserModel getClient(UserModel userModel, User user) {
        userModel.setName(user.getClient().getName());
        userModel.setSurname(user.getClient().getSurname());
        userModel.setFullName(user.getClient().getFullName());
        userModel.setGender(Gender.getName(user.getClient().getGender(), user.getLanguage()));
        userModel.setDob(user.getClient().getDob());
        userModel.setEmail(user.getClient().getEmail());
        userModel.setRole(ERole.ROLE_CLIENT);

//        if (user.getDeletedBy() != null) {
//            User admin = userService.getUserById(user.getDeletedBy());
//            String adminName = "Admin profile not found";
//            if (admin != null) {
//                adminName = admin.getAdmin().getFullName();
//            }
//            userModel.setDeletedBy(adminName);
//        }
        return userModel;
    }

    public ResponseEntity<?> registration(UserRequestModel userRequestModel, HttpServletRequest header) {
        try {
            UserModel userModel = userRequestModel.getUser();
            UserAgentModel userAgentModel = userRequestModel.getUserAgent();

            if (userService.existsByUsername(userModel.getMobileNumber())) {
                return OkResponse(StatusCodes.USER_ALREADY_REGISTERED);
            }

            Optional<Role> optClientRole = roleRepository.findByName(ERole.ROLE_CLIENT);
            if (!optClientRole.isPresent()) {
                return OkResponse(StatusCodes.USER_ROLE_NOT_FOUND);
            }

            ResponseModel<User> responseModel = userService.createUser(ERole.ROLE_CLIENT, userModel, userAgentModel, header);
            if (!responseModel.getSuccess()) {
                return InternalServerErrorResponse(responseModel.getException());
            }

            User user = responseModel.getData();

            userModel.clear();
            userModel.setIsOTPVerified(false);
            userModel.setDeviceId(responseModel.getDeviceId());
            userModel.setMobileNumber(user.getMaskedPhoneNumber());
            userModel.setMessage(getNameByLanguage(USER_OTP_NOT_VERIFIED, user.getLanguage()));

            return OkResponse(SUCCESS, userModel);
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> login(AuthenticationRequestModel authenticationRequestModel, HttpServletRequest header) {
        try {
            String jwt;
            User client;

            AuthenticationModel authenticationModel = authenticationRequestModel.getAuthentication();
            UserAgentModel userAgentModel = authenticationRequestModel.getUserAgent();

            try {
                client = userService.getUserByUsername(authenticationModel.getMobileNumber());
                if (client == null) {
                    return OkResponse(StatusCodes.NOT_FOUND);
                }

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(AES.encrypt(authenticationModel.getMobileNumber()),
                                authenticationModel.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                jwt = jwtUtils.generateJwtToken(authentication);
            } catch (Exception e) {
                logService.saveToLog(e);
                return BadRequestResponse(USER_PHONE_NUMBER_OR_PASSWORD_ERROR);
            }

            List<ERole> roles = client.getRoles().stream().map(Role::getName).collect(Collectors.toList());

            if (!roles.contains(ERole.ROLE_CLIENT)) {
                return UnauthorizedResponse(USER_IS_NOT_CLIENT);
            }

            UserModel userModel = new UserModel();
            boolean userAgentExists = userAgentService.checkIfUserAgentExists(authenticationModel.getDeviceId(), client);

            if (userAgentExists) {
                UserAgent userAgent = userAgentService.getUserAgentByEncodedId(authenticationModel.getDeviceId());

                if (userAgent != null && userAgent.isVerified()) {
                    userModel = userService.fromUserToUserModel(client);
                    userModel.setToken(jwt);
                    userModel.setIsOTPVerified(true);
                    userModel.setDeviceId(userAgent.getEncodedId());
                    userModel.setMessage(getNameByLanguage(USER_LOGGED_IN, client.getLanguage()));
                } else {
                    userModel.setIsOTPVerified(false);
                    userModel.setMobileNumber(client.getMaskedPhoneNumber());
                    userModel.setMessage(getNameByLanguage(USER_OTP_NOT_VERIFIED, client.getLanguage()));
                }
            } else {
                UserAgent userAgent = userAgentService.saveUserAgentInfo(client, userAgentModel, header);
                userModel.setIsOTPVerified(false);
                userModel.setDeviceId(userAgent.getEncodedId());
                userModel.setMobileNumber(client.getMaskedPhoneNumber());
                userModel.setMessage(getNameByLanguage(USER_OTP_NOT_VERIFIED, client.getLanguage()));
            }
            return OkResponse(SUCCESS, userModel);
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> getProfile(BasicModel basicModel, HttpServletRequest httpRequestHeader) {
        User client = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel clientModel = userService.fromUserToUserModel(client);

        return OkResponse(StatusCodes.SUCCESSFULLY_FOUND, clientModel);
    }

    public ResponseEntity<?> editProfile(UserModel userModel, HttpServletRequest httpRequestHeader) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userModel.getName() != null) {
            user.getClient().setName(userModel.getName());
        }

        user.getClient().setName(userModel.getName() == null ? user.getClient().getName() : userModel.getName());
        user.getClient().setSurname(userModel.getSurname() == null ? user.getClient().getSurname() : userModel.getSurname());
        user.getClient().setGender(userModel.getGender() == null ? user.getClient().getGender() : Gender.valueOf(userModel.getGender()));
        user.getClient().setDob(userModel.getDob() == null ? user.getClient().getDob() : userModel.getDob());
        user.getClient().setEmail(userModel.getEmail() == null ? user.getClient().getEmail() : userModel.getEmail());
        user.getClient().setFingerPrints(userModel.getFingerPrints() == null ? user.getClient().getFingerPrints() : userModel.getFingerPrints());
        user.getClient().setFaceScan(userModel.getFaceScan() == null ? user.getClient().getFaceScan() : userModel.getFaceScan());

        userService.saveUser(user);

        userModel = userService.fromUserToUserModel(user);
        return OkResponse(SUCCESS, userModel);
    }
}
