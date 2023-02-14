package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.tm.tashman.config.jwt.JwtUtils;
import uz.tm.tashman.entity.Admin;
import uz.tm.tashman.entity.Role;
import uz.tm.tashman.entity.User;
import uz.tm.tashman.entity.UserAgent;
import uz.tm.tashman.enums.ERole;
import uz.tm.tashman.enums.Gender;
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
public class AdminService extends HTTPUtil {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final UserAgentService userAgentService;
    private final LogService logService;

    @Autowired
    private UserService userService;


    public AdminService(
            JwtUtils jwtUtils,
            AuthenticationManager authenticationManager,
            RoleRepository roleRepository,
            UserAgentService userAgentService,
            LogService logService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.userAgentService = userAgentService;
        this.logService = logService;
    }

    public Admin createAdmin(UserModel userModel, User user) {
        Admin admin = new Admin();
        admin.setUser(user);
        admin.setName(userModel.getName());
        admin.setSurname(userModel.getSurname());
        admin.setDob(userModel.getDob());
        admin.setGender(Gender.getByCode(userModel.getGender()));
        admin.setCreatedDate(user.getCreatedDate());

        return admin;
    }

    public UserModel getAdmin(UserModel userModel, User user) {
        userModel.setName(user.getClient().getName());
        userModel.setSurname(user.getClient().getSurname());
        userModel.setFullName(user.getAdmin().getFullName());
        userModel.setGender(Gender.getNameByLanguage(user.getAdmin().getGender(), user.getLanguage()));
        userModel.setDob(user.getAdmin().getDob());
        userModel.setRole(ERole.ROLE_ADMIN);

        return userModel;
    }

    public ResponseEntity<?> registration(UserRequestModel userRequestModel, HttpServletRequest header) {
        try {
            UserModel userModel = userRequestModel.getUser();
            UserAgentModel userAgentModel = userRequestModel.getUserAgent();

            if (userService.existsByUsername(userModel.getMobileNumber())) {
                return OkResponse(USER_ALREADY_REGISTERED);
            }

            Optional<Role> optAdminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
            if (!optAdminRole.isPresent()) {
                return OkResponse(USER_ROLE_NOT_FOUND);
            }

            ResponseModel<User> responseModel = userService.createUser(ERole.ROLE_ADMIN, userModel, userAgentModel, header);
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
            User admin;
            String jwt;

            AuthenticationModel authenticationModel = authenticationRequestModel.getAuthentication();
            UserAgentModel userAgentModel = authenticationRequestModel.getUserAgent();

            try {
                admin = userService.getUserByUsername(authenticationModel.getMobileNumber());

                if (admin == null) {
                    return OkResponse(USER_NOT_FOUND);
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

            List<ERole> roles = admin.getRoles().stream().map(Role::getName).collect(Collectors.toList());

            if (!roles.contains(ERole.ROLE_ADMIN)) {
                return UnauthorizedResponse(USER_IS_NOT_ADMIN);
            }

            UserModel userModel = new UserModel();

            boolean userAgentExists = userAgentService.checkIfUserAgentExists(authenticationModel.getDeviceId(), admin);

            if (userAgentExists) {
                UserAgent userAgent = userAgentService.getUserAgentByEncodedId(authenticationModel.getDeviceId());

                if (userAgent != null && userAgent.isVerified()) {
                    userModel = userService.fromUserToUserModel(admin);
                    userModel.setToken(jwt);
                    userModel.setIsOTPVerified(true);
                    userModel.setDeviceId(userAgent.getEncodedId());
                    userModel.setMessage(getNameByLanguage(USER_LOGGED_IN, admin.getLanguage()));
                } else {
                    userModel.setIsOTPVerified(false);
                    userModel.setMobileNumber(admin.getMaskedPhoneNumber());
                    userModel.setMessage(getNameByLanguage(USER_OTP_NOT_VERIFIED, admin.getLanguage()));
                }
            } else {
                UserAgent userAgent = userAgentService.saveUserAgentInfo(admin, userAgentModel, header);
                userModel.setIsOTPVerified(false);
                userModel.setDeviceId(userAgent.getEncodedId());
                userModel.setMobileNumber(admin.getMaskedPhoneNumber());
                userModel.setMessage(getNameByLanguage(USER_OTP_NOT_VERIFIED, admin.getLanguage()));
            }
            return OkResponse(SUCCESS, userModel);
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> deleteUser(BasicModel basicModel) {
        try {
            User user = userService.getUserById(basicModel.getId());

            return OkResponse(SUCCESS);
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }
}