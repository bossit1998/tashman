package uz.tm.tashman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.tm.tashman.config.jwt.JwtUtils;
import uz.tm.tashman.dao.*;
import uz.tm.tashman.entity.*;
import uz.tm.tashman.enums.ERole;
import uz.tm.tashman.enums.Platform;
import uz.tm.tashman.models.requestModels.AuthenticationRequest;
import uz.tm.tashman.models.requestModels.UserRegisterRequest;
import uz.tm.tashman.models.responseModels.UserRegisterResponse;
import uz.tm.tashman.models.wrapModels.ErrorResponse;
import uz.tm.tashman.models.wrapModels.SuccessResponse;
import uz.tm.tashman.util.StringUtil;
import uz.tm.tashman.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService {

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

//    @Value("${local.file.path}")
//    String filePath;

    @Autowired
    UserAgentRepository userAgentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserPlatformRepository userPlatformRepository;

    public ResponseEntity<?> registration(UserRegisterRequest userRegisterRequest,
                                          HttpServletRequest request) {
        if (userRepository.existsByUsername(userRegisterRequest.getMobileNumber())) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, "User is Already Registered!"));
        }
        // Create new user's account
        User user = new User(userRegisterRequest.getMobileNumber(), encoder.encode(userRegisterRequest.getPassword()));
//        User user = new User(userRegisterRequestModel.getMobileNumber(), StringUtil.encodePassword(userRegisterRequestModel.getPassword()));

        Role patientRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Patient Role is not found."));

        Set<Role> roles = new HashSet<>();

        roles.add(patientRole);

//        String otp = Util.otpGeneration();
        String otp = "1234";
        user.setRoles(roles);
        user.setCreatedDate(LocalDateTime.now());
        user.setOtp(otp);

        UserAgent userAgent = new UserAgent();
        userAgent.setUserAgent(request.getHeader("User-Agent"));
        userAgent.setTokenDate(LocalDateTime.now());
        userAgent.setVerified(false);

        Client patient = new Client();
        patient.setFullName(userRegisterRequest.getFullName());
        patient.setFaceScan(userRegisterRequest.getFaceScan());
        patient.setFingerPrints(userRegisterRequest.getFingerPrints());
        patient.setEmail(userRegisterRequest.getEmail());
        patient.setDob(userRegisterRequest.getDob());
        patient.setGender(userRegisterRequest.getGender());

        patient.setIsActive(true);
        patient.setUser(user);
        user.setClient(patient);
        user = userRepository.save(user);
//        ledgerAccountService.createAccount(user.getId());
        userAgent.setUser(user);
        userAgentRepository.save(userAgent);

//        if (!StringUtils.isEmpty(patientRegisternRequest.getPlatform())
//                && !StringUtils.isEmpty(patientRegisternRequest.getFcmToken())) {
//            List<UserPlatform> deleteUserPlatformList = userPlatformRepository.findByFcmTokenOrVoipToken(
//                    patientRegisternRequest.getFcmToken(),
//                    patientRegisternRequest.getVoipToken() == null ? "" : patientRegisternRequest.getVoipToken());
//
//            UserPlatform userPlatform = new UserPlatform();
//            userPlatform.setFcmToken(patientRegisternRequest.getFcmToken());
//            userPlatform.setVoipToken(patientRegisternRequest.getVoipToken());
//            userPlatform.setUserId(user.getId());
//            if (patientRegisternRequest.getPlatform().equalsIgnoreCase(Platform.ANDROID.name())) {
//                userPlatform.setPlatform(Platform.ANDROID);
//            } else if (patientRegisternRequest.getPlatform().equalsIgnoreCase(Platform.WEB.name())) {
//                userPlatform.setPlatform(Platform.WEB);
//            } else if (patientRegisternRequest.getPlatform().equalsIgnoreCase(Platform.IOS.name())) {
//                userPlatform.setPlatform(Platform.IOS);
//            }
//            if (userPlatform.getPlatform() != null) {
//                userPlatformRepository.deleteAll(deleteUserPlatformList);
//                userPlatformRepository.save(userPlatform);
//            }
//        }

//        PatientRegisterResponse patientRegisterResponse = new PatientRegisterResponse(
//                "Registered Successfully. Please Verify your otp.", StringUtil.maskPhoneNumber(user.getUsername()));
//        smsService.sendOtp(patientRegisternRequest.getMobileNumber(), otp);
//        patientRegisterResponse.setIsOTPVerified(false);
        return ResponseEntity.ok(new SuccessResponse<>(200, "Registered Successfully. Please Verify your otp.", StringUtil.maskPhoneNumber(user.getUsername())));
    }

    public ResponseEntity<?> login(AuthenticationRequest authenticationRequest, HttpServletRequest request) {
        String jwt;

        Optional<User> optionUser;

        try {
            optionUser = userRepository.findByUsername(authenticationRequest.getMobileNumber());
            if (!optionUser.isPresent()) {
                return ResponseEntity.badRequest().body(new ErrorResponse(400, "User doesn't exist"));
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getMobileNumber(), authenticationRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            jwt = jwtUtils.generateJwtToken(authentication);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Phone number or password error"));
        }

        User user = optionUser.get();

        List<ERole> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        if (!roles.contains(ERole.ROLE_USER)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(401, "User is not a Client!"));
        }

        if (!StringUtils.isEmpty(authenticationRequest.getPlatform())
                && !StringUtils.isEmpty(authenticationRequest.getFcmToken())) {
            List<UserPlatform> deleteUserPlatformList = userPlatformRepository.findByFcmTokenOrVoipToken(
                    authenticationRequest.getFcmToken(),
                    authenticationRequest.getVoipToken() == null ? "" : authenticationRequest.getVoipToken());

            UserPlatform userPlatform = new UserPlatform();
            userPlatform.setFcmToken(authenticationRequest.getFcmToken());
            userPlatform.setVoipToken(authenticationRequest.getVoipToken());
            userPlatform.setUserId(user.getId());
            if (authenticationRequest.getPlatform().equalsIgnoreCase(Platform.ANDROID.name())) {
                userPlatform.setPlatform(Platform.ANDROID);
            } else if (authenticationRequest.getPlatform().equalsIgnoreCase(Platform.WEB.name())) {
                userPlatform.setPlatform(Platform.WEB);
            } else if (authenticationRequest.getPlatform().equalsIgnoreCase(Platform.IOS.name())) {
                userPlatform.setPlatform(Platform.IOS);
            }
            if (userPlatform.getPlatform() != null) {
                userPlatformRepository.deleteAll(deleteUserPlatformList);
                userPlatformRepository.save(userPlatform);
            }
        }

        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
        userRegisterResponse.setIsOTPVerified(true);

        UserAgent userAgent = userAgentRepository.findByUserAndUserAgent(user, request.getHeader("User-Agent"));

        if (userAgent != null && !userAgent.isDeleted()) {
            if (!userAgent.isVerified()) {
                userAgent.setTokenDate(LocalDateTime.now());
                String otp = Util.otpGeneration();
                user.setOtp(otp);
                userRepository.save(user);
                userAgent.setUser(user);
                userAgentRepository.save(userAgent);
//                smsService.sendOtp(authenticationRequest.getMobileNumber(), otp);
                userRegisterResponse.setIsOTPVerified(false);
                userRegisterResponse.setMobileNumber(StringUtil.maskPhoneNumber(user.getUsername()));
            }
        } else {
            userAgent = new UserAgent();
            userAgent.setUserAgent(request.getHeader("User-Agent"));
            userAgent.setTokenDate(LocalDateTime.now());
            userAgent.setVerified(false);
            String otp = Util.otpGeneration();
            user.setOtp(otp);
            userRepository.save(user);
            userAgent.setUser(user);
            userAgentRepository.save(userAgent);
//            smsService.sendOtp(authenticationRequest.getMobileNumber(), otp);
            userRegisterResponse.setIsOTPVerified(false);
            userRegisterResponse.setMobileNumber(StringUtil.maskPhoneNumber(user.getUsername()));
        }


        if (userRegisterResponse.getIsOTPVerified()) {
            userRegisterResponse.setToken(jwt);
            Client client = user.getClient();
            userRegisterResponse.setFaceScan(client.getFaceScan());
            userRegisterResponse.setDob(client.getDob());
            userRegisterResponse.setFingerPrints(client.getFingerPrints());
            userRegisterResponse.setFullName(client.getFullName());
            userRegisterResponse.setGender(client.getGender());
            userRegisterResponse.setMobileNumber(authenticationRequest.getMobileNumber());
            userRegisterResponse.setId(user.getId());
            userRegisterResponse.setProfileImageUrl(user.getProfileImage());
            userRegisterResponse.setIsActive(client.getIsActive());
            if (user.getClient().getAddress() != null) {
                userRegisterResponse.setStreet1(client.getAddress().getStreet());
                userRegisterResponse.setStreet2(client.getAddress().getDistrict());
                userRegisterResponse.setCity(client.getAddress().getCity());
                userRegisterResponse.setZipCode(client.getAddress().getRegion());
                userRegisterResponse.setState(client.getAddress().getCountry());
                userRegisterResponse.setCountry(client.getAddress().getZipCode());
                userRegisterResponse.setLatitude(user.getClient().getLatitude());
                userRegisterResponse.setLongitude(user.getClient().getLongitude());
            }
        } else {
            userRegisterResponse.setMessage("please verify otp.");
        }
        return ResponseEntity.ok(userRegisterResponse);

    }
}