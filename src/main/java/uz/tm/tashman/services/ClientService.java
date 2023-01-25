package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.tm.tashman.config.jwt.JwtUtils;
import uz.tm.tashman.entity.Client;
import uz.tm.tashman.entity.Role;
import uz.tm.tashman.entity.User;
import uz.tm.tashman.entity.UserAgent;
import uz.tm.tashman.enums.ERole;
import uz.tm.tashman.enums.Gender;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.AuthenticationModel;
import uz.tm.tashman.models.UserModel;
import uz.tm.tashman.models.wrapperModels.ErrorResponse;
import uz.tm.tashman.repository.*;
import uz.tm.tashman.util.AES;
import uz.tm.tashman.util.HTTPUtil;
import uz.tm.tashman.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientService extends HTTPUtil {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Value("${local.file.path}")
    String filePath;

    @Autowired
    UserAgentRepository userAgentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserService userService;

    public Client createClient(UserModel userModel, User user) {
        Client client = new Client();
        client.setFullName(userModel.getFullName());
        client.setDob(userModel.getDob());
        client.setGender(Gender.getByCode(userModel.getGender()));
        client.setCreatedDate(user.getCreatedDate());

        return client;
    }

    public UserModel getClient(UserModel userModel, User user) {
        userModel.setFullName(user.getClient().getFullName());
        userModel.setGender(Gender.getNameByLanguage(user.getClient().getGender(), user.getLanguage()));
        userModel.setDob(user.getClient().getDob());
        userModel.setEmail(user.getClient().getEmail());

        if (user.getDeletedBy() != null) {
            User admin = userService.getUserById(user.getDeletedBy());
            String adminName = "Admin profile not found";
            if (admin != null) {
                adminName = admin.getAdmin().getFullName();
            }
            userModel.setDeletedBy(adminName);
        }

        return userModel;
    }


    public ResponseEntity<?> registration(UserModel userModel, HttpServletRequest request) {
        if (userRepository.existsByUsername(userModel.getMobileNumber())) {
            return OkResponse(StatusCodes.USER_ALREADY_REGISTERED);
        }

        User user = new User(AES.encrypt(userModel.getMobileNumber()), encoder.encode(userModel.getPassword()), null);

        Optional<Role> optPatientRole = roleRepository.findByName(ERole.ROLE_USER);
        if (!optPatientRole.isPresent()) {
            return OkResponse(StatusCodes.USER_ROLE_NOT_FOUND);
        }

        Role patientRole = optPatientRole.get();

        Set<Role> roles = new HashSet<>();
        roles.add(patientRole);

        // todo - should be uncommented to generate otp
//        String otp = Util.otpGeneration();

        String otp = "1234";

        user.setRoles(roles);
        user.setCreatedDate(LocalDateTime.now());
//        user.setOtp(otp);

        UserAgent userAgent = new UserAgent();
        userAgent.setUserAgent(request.getHeader("User-Agent"));
        userAgent.setTokenDate(LocalDateTime.now());
        userAgent.setVerified(false);


        Client client = new Client();
        client.setFullName(userModel.getFullName());
        client.setUser(user);

        user.setClient(client);

//        user.getAdmin().setFaceScan(userModel.getFaceScan());
//        user.getAdmin().setFingerPrint(userModel.getFingerPrints());
//        user.getAdmin().setEmail(userModel.getEmail());
//        user.getAdmin().setDob(userModel.getDob());
//        user.getAdmin().setGender(userModel.getGender());
        user = userRepository.save(user);

        // todo - implement account
//        ledgerAccountService.createAccount(user.getId());

        userAgent.setUser(user);
        userAgentRepository.save(userAgent);

//        if (!StringUtils.isEmpty(userModel.getPlatform()) &&
//                !StringUtils.isEmpty(userModel.getFcmToken())) {
//            List<UserPlatform> deleteUserPlatformList = userPlatformRepository.findByFcmTokenOrVoipToken(
//                    userModel.getFcmToken(),
//                    userModel.getVoipToken() == null ? "" : userModel.getVoipToken());
//
//            UserPlatform userPlatform = new UserPlatform();
//            userPlatform.setFcmToken(userModel.getFcmToken());
//            userPlatform.setVoipToken(userModel.getVoipToken());
//            userPlatform.setUserId(user.getId());
//            if (userModel.getPlatform().equalsIgnoreCase(Platform.ANDROID.name())) {
//                userPlatform.setPlatform(Platform.ANDROID);
//            } else if (userModel.getPlatform().equalsIgnoreCase(Platform.WEB.name())) {
//                userPlatform.setPlatform(Platform.WEB);
//            } else if (userModel.getPlatform().equalsIgnoreCase(Platform.IOS.name())) {
//                userPlatform.setPlatform(Platform.IOS);
//            }
//            if (userPlatform.getPlatform() != null) {
//                userPlatformRepository.deleteAll(deleteUserPlatformList);
//                userPlatformRepository.save(userPlatform);
//            }
//        }

        return OkResponse(StatusCodes.SUCCESS, Util.maskPhoneNumber(user.getUsername()));
    }

    public ResponseEntity<?> login(AuthenticationModel authenticationModel, HttpServletRequest request) {
        String jwt;

        Optional<User> optionalUser;

        try {
            optionalUser = userRepository.findByUsername(authenticationModel.getMobileNumber());

            if (!optionalUser.isPresent()) {
                return OkResponse(StatusCodes.NOT_FOUND);
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationModel.getMobileNumber(), authenticationModel.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            jwt = jwtUtils.generateJwtToken(authentication);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Phone number or password error"));
        }

        User user = optionalUser.get();

        List<ERole> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        if (!roles.contains(ERole.ROLE_USER)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(401, "User is not a Client!"));
        }

//        if (!StringUtils.isEmpty(authenticationModel.getPlatform())
//                && !StringUtils.isEmpty(authenticationModel.getFcmToken())) {
//            List<UserPlatform> deleteUserPlatformList = userPlatformRepository.findByFcmTokenOrVoipToken(
//                    authenticationModel.getFcmToken(),
//                    authenticationModel.getVoipToken() == null ? "" : authenticationModel.getVoipToken());
//
//            UserPlatform userPlatform = new UserPlatform();
//            userPlatform.setFcmToken(authenticationModel.getFcmToken());
//            userPlatform.setVoipToken(authenticationModel.getVoipToken());
//            userPlatform.setUserId(user.getId());
//            if (authenticationModel.getPlatform().equalsIgnoreCase(Platform.ANDROID.name())) {
//                userPlatform.setPlatform(Platform.ANDROID);
//            } else if (authenticationModel.getPlatform().equalsIgnoreCase(Platform.WEB.name())) {
//                userPlatform.setPlatform(Platform.WEB);
//            } else if (authenticationModel.getPlatform().equalsIgnoreCase(Platform.IOS.name())) {
//                userPlatform.setPlatform(Platform.IOS);
//            }
//            if (userPlatform.getPlatform() != null) {
//                userPlatformRepository.deleteAll(deleteUserPlatformList);
//                userPlatformRepository.save(userPlatform);
//            }
//        }

        UserModel userRegisterResponse = new UserModel();
        userRegisterResponse.setIsOTPVerified(true);

        Optional<UserAgent> optUserAgent = userAgentRepository.findByUserAndUserAgent(user, request.getHeader("User-Agent"));
        if (!optionalUser.isPresent()) {
            return OkResponse(StatusCodes.NOT_FOUND);
        }
        UserAgent userAgent = optUserAgent.get();

        if (!userAgent.getIsDeleted()) {
            if (!userAgent.isVerified()) {
                userAgent.setTokenDate(LocalDateTime.now());
                // todo - should be uncommented to generate otp
//                String otp = Util.otpGeneration();
                String otp = "1234";
//                user.setOtp(otp);
                userRepository.save(user);
                userAgent.setUser(user);
                userAgentRepository.save(userAgent);
//                smsService.sendOtp(authenticationRequest.getMobileNumber(), otp);
                userRegisterResponse.setIsOTPVerified(false);
                userRegisterResponse.setMobileNumber(Util.maskPhoneNumber(user.getUsername()));
            }
        } else {
            userAgent = new UserAgent();
            userAgent.setUserAgent(request.getHeader("User-Agent"));
            userAgent.setTokenDate(LocalDateTime.now());
            userAgent.setVerified(false);
            String otp = Util.otpGeneration();
//            user.setOtp(otp);
            userRepository.save(user);
            userAgent.setUser(user);
            userAgentRepository.save(userAgent);
            // todo - should be uncommented to send sms
//            smsService.sendOtp(authenticationRequest.getMobileNumber(), otp);
            userRegisterResponse.setIsOTPVerified(false);
            userRegisterResponse.setMobileNumber(Util.maskPhoneNumber(user.getUsername()));
        }


        if (userRegisterResponse.getIsOTPVerified()) {
            userRegisterResponse.setToken(jwt);
//            userRegisterResponse.setFaceScan(user.getClient().getFaceScan());
//            userRegisterResponse.setDob(user.getClient().getDob().toString());
//            userRegisterResponse.setFingerPrints(user.getClient().getFingerPrint());
//            userRegisterResponse.setFullName(user.getClient().getFullName());
//            userRegisterResponse.setGender(user.getClient().getGender());
            userRegisterResponse.setMobileNumber(authenticationModel.getMobileNumber());
            userRegisterResponse.setId(user.getId());
            userRegisterResponse.setProfileImageUrl(user.getProfileImageUrl());
//            userRegisterResponse.setIsActive(user.getClient().getIsActive());
//            if (user.getAddress() != null) {
//                userRegisterResponse.setCity(user.getAddress().getCity());
//                userRegisterResponse.setZipCode(user.getAddress().getRegion());
//                userRegisterResponse.setCountry(user.getAddress().getZipCode());
//                userRegisterResponse.setLatitude(user.getuser().getLatitude());
//                userRegisterResponse.setLongitude(user.getuser().getLongitude());
//            }
        } else {
            userRegisterResponse.setMessage("please verify otp.");
        }
        return OkResponse(StatusCodes.USER_LOGGED_IN, userRegisterResponse);
    }
}