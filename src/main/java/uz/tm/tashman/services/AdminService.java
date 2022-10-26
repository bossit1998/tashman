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
import uz.tm.tashman.models.AuthenticationModel;
import uz.tm.tashman.models.ResponseModel;
import uz.tm.tashman.models.UserModel;
import uz.tm.tashman.repository.RoleRepository;
import uz.tm.tashman.repository.UserRepository;
import uz.tm.tashman.util.AES;
import uz.tm.tashman.util.HTTPUtil;
import uz.tm.tashman.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static uz.tm.tashman.enums.StatusCodes.*;

@Service
public class AdminService extends HTTPUtil {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserAgentService userAgentService;
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;

    public Admin createAdmin(UserModel userModel, User user) {
        Admin admin = new Admin();
        admin.setUser(user);
        admin.setFullName(userModel.getFullName());
        admin.setDob(userModel.getDob());
        admin.setGender(Gender.getByCode(userModel.getGender()));
        admin.setCreatedDate(user.getCreatedDate());

        return admin;
    }

    public UserModel getAdmin(UserModel userModel, User user) {
        userModel.setFullName(user.getAdmin().getFullName());
        userModel.setGender(Gender.getNameByLanguage(user.getAdmin().getGender(), user.getLanguage()));
        userModel.setDob(user.getAdmin().getDob());

        return userModel;
    }

    public ResponseEntity<?> registration(UserModel userModel, HttpServletRequest header) {
        try {
            if (userRepository.existsByUsername(AES.encrypt(userModel.getMobileNumber()))) {
                return OkResponse(USER_ALREADY_REGISTERED);
            }

            Optional<Role> optAdminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
            if (!optAdminRole.isPresent()) {
                return OkResponse(USER_ROLE_NOT_FOUND);
            }

            ResponseModel<User> responseModel = userService.createUser(ERole.ROLE_ADMIN, userModel, header);
            if (!responseModel.getSuccess()) {
                return InternalServerErrorResponse(exceptionAsString(responseModel.getException()));
            }

            User user = responseModel.getData();

            userModel.clear();
            userModel.setIsOTPVerified(false);
            userModel.setDeviceId(responseModel.getDeviceId());
            userModel.setMobileNumber(user.getMaskedPhoneNumber());
            userModel.setMessage(getNameByLanguage(USER_OTP_NOT_VERIFIED, user.getLanguage()));

            return OkResponse(SUCCESS, userModel);
        } catch (Exception e) {
            logService.saveToLog(e);
            return InternalServerErrorResponse(exceptionAsString(e));
        }
    }

    public ResponseEntity<?> login(AuthenticationModel authenticationModel, HttpServletRequest header) {
        try {
            User admin;
            String jwt;

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
                    userModel = userService.getUserModel(admin);
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
                UserAgent userAgent = userAgentService.saveUserAgentInfo(admin, authenticationModel.getUserAgentModel(), header);
                userModel.setIsOTPVerified(false);
                userModel.setDeviceId(userAgent.getEncodedId());
                userModel.setMobileNumber(admin.getMaskedPhoneNumber());
                userModel.setMessage(getNameByLanguage(USER_OTP_NOT_VERIFIED, admin.getLanguage()));
            }
            return OkResponse(SUCCESS, userModel);
        } catch (Exception e) {
            logService.saveToLog(e);
            return InternalServerErrorResponse(exceptionAsString(e));
        }
    }
}