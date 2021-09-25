/*
package uz.tm.tashman.service;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.omg.IOP.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.tm.tashman.config.jwt.JwtUtils;
import uz.tm.tashman.dao.UserAgentRepository;
import uz.tm.tashman.dao.UserPlatformRepository;
import uz.tm.tashman.dao.UserRepository;
import uz.tm.tashman.entity.User;
import uz.tm.tashman.entity.UserAgent;
import uz.tm.tashman.entity.UserPlatform;
import uz.tm.tashman.enums.ERole;
import uz.tm.tashman.models.*;
import uz.tm.tashman.util.StringUtil;
import uz.tm.tashman.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonService {

//	private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserAgentRepository userAgentRepository;

    @Autowired
    UserPlatformRepository userPlatformRepository;

    @Autowired
    JwtUtils jwtUtils;


    @Autowired
    TransactionService transactionService;

//    @Autowired
//    DrugDoseUnitsRepository drugDoseUnitsRepository;

    @Value("${local.file.path}")
    String fileLocation;


    public ResponseEntity<?> forgotPassword(AuthenticationRequest authenticationRequest) {
        Optional<User> user = userRepository.findByUsername(authenticationRequest.getMobileNumber());
        if (!user.isPresent()) {
            return ResponseEntity.ok(new DoctorRegisterResponse("User does not exist!"));
        }

        DoctorRegisterResponse doctorRegisterResponse = new DoctorRegisterResponse();
        String otp = Util.otpGeneration();
        user.get().setOtp(otp);
        userRepository.save(user.get());
        smsService.sendOtp(authenticationRequest.getMobileNumber(), otp);
        doctorRegisterResponse.setMobileNumber(StringUtil.maskPhoneNumber(authenticationRequest.getMobileNumber()));
        doctorRegisterResponse.setMessage("OTP sent successfully!");
        return ResponseEntity.ok(doctorRegisterResponse);
    }


    public ResponseEntity<DoctorRegisterResponse> changePassword(AuthenticationRequest authenticationRequest) {
        Optional<User> user = userRepository.findByUsername(authenticationRequest.getMobileNumber());
        if (!user.isPresent()) {
            return ResponseEntity.ok(new DoctorRegisterResponse("User does not exist!"));
        }
        DoctorRegisterResponse doctorRegisterResponse = new DoctorRegisterResponse();
        doctorRegisterResponse.setIsOTPVerified(false);
        doctorRegisterResponse.setMessage("Wrong OTP entered!");
        if (authenticationRequest.getOtp().equals((user).get().getOtp())) {
            user.get().setPassword(encoder.encode(authenticationRequest.getPassword()));
            userRepository.save(user.get());
            doctorRegisterResponse.setIsOTPVerified(true);
            doctorRegisterResponse.setMessage("Password Changed Successfully!");
        }
        return ResponseEntity.ok(doctorRegisterResponse);
    }


    public ResponseEntity<?> otpValidated(OtpValidationRequest otpValidationRequest, HttpServletRequest request,
                                          String language) {

        Optional<User> user = userRepository.findByUsername(otpValidationRequest.getMobileNumber());

        if (!user.isPresent()) {
            ResponseEntity.badRequest().body(new UniversalErrorResponseDTO(400, "User not found"));
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.get(), null,
                user.get().getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserAgent userAgent = userAgentRepository.findByUserAndUserAgent(user.get(), request.getHeader("User-Agent"));

        if (userAgent == null) {
            return ResponseEntity.badRequest().body(new UniversalErrorResponseDTO(400, "There is no such device"));
        }
        if (userAgent.isVerified()) {
            return ResponseEntity.badRequest().body(new UniversalErrorResponseDTO(400, "This device is already verified"));
        } // return token and login to system
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setIsOTPVerified(true);

        if (otpValidationRequest.getOtp().equals(user.get().getOtp())) {
            if (user.get().getPatient() != null) {
                // user is patient

                userAgent.setVerified(true);
                userAgent.setUser(user.get());
                userAgentRepository.save(userAgent);

                PatientRegisterResponse patientRegisterResponse = new PatientRegisterResponse();
                patientRegisterResponse.setIsOTPVerified(true);
                patientRegisterResponse.setMessage("OTP verified Successfully!");
                patientRegisterResponse.setToken(jwt);
                patientRegisterResponse.setFaceScan(user.get().getPatient().getFaceScan());
                patientRegisterResponse.setDob(user.get().getPatient().getDob());
                patientRegisterResponse.setFingerPrints(user.get().getPatient().getFingerPrints());
                patientRegisterResponse.setFullName(user.get().getPatient().getFullName());
                patientRegisterResponse.setGender(user.get().getPatient().getGender());
                patientRegisterResponse.setMobileNumber(user.get().getUsername());
                patientRegisterResponse.setId(user.get().getId());
                if (user.get().getPatient().getAddress() != null) {
                    patientRegisterResponse.setStreet1(user.get().getPatient().getAddress().getStreet1());
                    patientRegisterResponse.setStreet2(user.get().getPatient().getAddress().getStreet2());
                    patientRegisterResponse.setCity(user.get().getPatient().getAddress().getCity());
                    patientRegisterResponse.setZipCode(user.get().getPatient().getAddress().getZipCode());
                    patientRegisterResponse.setState(user.get().getPatient().getAddress().getState());
                    patientRegisterResponse.setCountry(user.get().getPatient().getAddress().getCountry());
                    patientRegisterResponse.setLatitude(user.get().getPatient().getLatitude());
                    patientRegisterResponse.setLongitude(user.get().getPatient().getLongitude());
                }

                return ResponseEntity.ok(patientRegisterResponse);

            } else if (user.get().getDoctor() != null) {
                // user is doctor

                userAgent.setVerified(true);
                userAgent.setUser(user.get());
                userAgentRepository.save(userAgent);

                DoctorRegisterResponse doctorRegisterResponse = new DoctorRegisterResponse();
                doctorRegisterResponse.setIsOTPVerified(true);
                doctorRegisterResponse.setMessage("OTP verified Successfully!");
                doctorRegisterResponse.setToken(jwt);
                doctorRegisterResponse.setFaceScan(user.get().getDoctor().getFaceScan());
                doctorRegisterResponse.setDob(user.get().getDoctor().getDob());
                doctorRegisterResponse.setFee(user.get().getDoctor().getFee());
                doctorRegisterResponse.setFingerPrints(user.get().getDoctor().getFingerPrints());
                doctorRegisterResponse.setFullName(user.get().getDoctor().getFullName());
                doctorRegisterResponse.setEmail(user.get().getDoctor().getEmail());
                doctorRegisterResponse.setGender(user.get().getDoctor().getGender());
                doctorRegisterResponse.setMobileNumber(user.get().getUsername());
                doctorRegisterResponse.setIsOnline(user.get().getDoctor().getIsOnline());
                List<String> specialitiesAsString = new ArrayList<>();
                List<DoctorSpecialities> specs = doctorSpecialitiesRepository.findByDoctorId(user.get().getId());
                switch (language) {
                    case "us":
                        specs.forEach(spec -> {
                            specialitiesAsString.add(spec.getEnglishName());
                        });
                        break;
                    case "ru":
                        specs.forEach(spec -> {
                            specialitiesAsString.add(spec.getRussianName());
                        });
                        break;
                    case "uz":
                        specs.forEach(spec -> {
                            specialitiesAsString.add(spec.getUzbekName());
                        });
                        break;
                }
                doctorRegisterResponse.setSpeciality(specialitiesAsString);
                doctorRegisterResponse.setActivityType(user.get().getDoctor().getActivityType());

                doctorRegisterResponse.setLicence(user.get().getDoctor().getLicence());
                doctorRegisterResponse.setAboutSelf(user.get().getDoctor().getAboutSelf());
                doctorRegisterResponse.setExperience(user.get().getDoctor().getExperience());
                doctorRegisterResponse.setIsActive(user.get().getDoctor().getIsActive());
                doctorRegisterResponse.setCertificate(user.get().getDoctor().getCertificate());
                doctorRegisterResponse.setSsn(user.get().getDoctor().getSsn());
                doctorRegisterResponse.setCreatedBy(user.get().getDoctor().getCreatedBy());
                doctorRegisterResponse.setStreet1(user.get().getDoctor().getAddress().getStreet1());
                doctorRegisterResponse.setStreet2(user.get().getDoctor().getAddress().getStreet2());
                doctorRegisterResponse.setCity(user.get().getDoctor().getAddress().getCity());
                doctorRegisterResponse.setZipCode(user.get().getDoctor().getAddress().getZipCode());
                doctorRegisterResponse.setState(user.get().getDoctor().getAddress().getState());
                doctorRegisterResponse.setCountry(user.get().getDoctor().getAddress().getCountry());
                doctorRegisterResponse.setLatitude(user.get().getDoctor().getLatitude());
                doctorRegisterResponse.setLongitude(user.get().getDoctor().getLongitude());
                doctorRegisterResponse.setId(user.get().getId());
                return ResponseEntity.ok(doctorRegisterResponse);
            } else if (user.get().getAccountant() != null) {
                // user is patient

                userAgent.setVerified(true);
                userAgent.setUser(user.get());
                userAgentRepository.save(userAgent);

                registerResponse.setDob(user.get().getAccountant().getDob());
                registerResponse.setFullName(user.get().getAccountant().getFullName());
                registerResponse.setGender(user.get().getAccountant().getGender());
                registerResponse.setSsn(user.get().getAccountant().getSsn());
                registerResponse.setIsActive(user.get().getAccountant().getIsActive());
                registerResponse.setFingerPrints(user.get().getAccountant().getFingerPrints());
                registerResponse.setCreatedBy(user.get().getAccountant().getCreatedBy());
                registerResponse.setId(user.get().getId());
                registerResponse.setMobileNumber(user.get().getUsername());

                return ResponseEntity.ok(registerResponse);

            } else if (user.get().getMonitoring() != null) {
                // user is patient

                userAgent.setVerified(true);
                userAgent.setUser(user.get());
                userAgentRepository.save(userAgent);

                registerResponse.setDob(user.get().getMonitoring().getDob());
                registerResponse.setFullName(user.get().getMonitoring().getFullName());
                registerResponse.setGender(user.get().getMonitoring().getGender());
                registerResponse.setSsn(user.get().getMonitoring().getSsn());
                registerResponse.setIsActive(user.get().getMonitoring().getIsActive());
                registerResponse.setFingerPrints(user.get().getMonitoring().getFingerPrints());
                registerResponse.setCreatedBy(user.get().getMonitoring().getCreatedBy());
                registerResponse.setId(user.get().getId());
                registerResponse.setMobileNumber(user.get().getUsername());

                return ResponseEntity.ok(registerResponse);

            } else if (user.get().getSecurity() != null) {
                // user is patient

                userAgent.setVerified(true);
                userAgent.setUser(user.get());
                userAgentRepository.save(userAgent);

                registerResponse.setDob(user.get().getSecurity().getDob());
                registerResponse.setFullName(user.get().getSecurity().getFullName());
                registerResponse.setGender(user.get().getSecurity().getGender());
                registerResponse.setSsn(user.get().getSecurity().getSsn());
                registerResponse.setIsActive(user.get().getSecurity().getIsActive());
                registerResponse.setFingerPrints(user.get().getSecurity().getFingerPrints());
                registerResponse.setCreatedBy(user.get().getSecurity().getCreatedBy());
                registerResponse.setId(user.get().getId());
                registerResponse.setMobileNumber(user.get().getUsername());

                return ResponseEntity.ok(registerResponse);

            } else if (user.get().getAdmin() != null) {
                // user is patient

                userAgent.setVerified(true);
                userAgent.setUser(user.get());
                userAgentRepository.save(userAgent);

                registerResponse.setDob(user.get().getAdmin().getDob());
                registerResponse.setFullName(user.get().getAdmin().getFullName());
                registerResponse.setGender(user.get().getAdmin().getGender());
                registerResponse.setSsn(user.get().getAdmin().getSsn());
                registerResponse.setIsActive(user.get().getAdmin().getIsActive());
                registerResponse.setFingerPrints(user.get().getAdmin().getFingerPrints());
                registerResponse.setCreatedBy(user.get().getAdmin().getCreatedBy());
                registerResponse.setId(user.get().getId());
                registerResponse.setMobileNumber(user.get().getUsername());

                return ResponseEntity.ok(registerResponse);

            } else if (user.get().getMainDoctor() != null) {
                // user is patient

                userAgent.setVerified(true);
                userAgent.setUser(user.get());
                userAgentRepository.save(userAgent);

                registerResponse.setDob(user.get().getMainDoctor().getDob());
                registerResponse.setFullName(user.get().getMainDoctor().getFullName());
                registerResponse.setGender(user.get().getMainDoctor().getGender());
                registerResponse.setSsn(user.get().getMainDoctor().getSsn());
                registerResponse.setMobileNumber(user.get().getUsername());
                registerResponse.setIsActive(user.get().getMainDoctor().getIsActive());
                registerResponse.setFingerPrints(user.get().getMainDoctor().getFingerPrints());
                registerResponse.setCreatedBy(user.get().getMainDoctor().getCreatedBy());
                registerResponse.setId(user.get().getId());
                return ResponseEntity.ok(registerResponse);

            }

        } else {
            return ResponseEntity.badRequest().body(new PatientRegisterResponse("Wrong otp entered"));
        }
        return ResponseEntity.badRequest().body(new PatientRegisterResponse("some error occurred"));
    }


    public ResponseEntity<?> setPinCode(PinCodeRequest pinCodeRequest, HttpServletRequest request) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserAgent currentUserAgent = userAgentRepository.findByUserAndUserAgent(user,
                    request.getHeader("User-Agent"));

            if (currentUserAgent.isDeletedStatus()) {
                // TODO Login
                return ResponseEntity
                        .ok(new ApiResponseModel(HttpStatus.BAD_REQUEST.value(), false, "User agent data is deleted"));
            }
            if (!currentUserAgent.isVerified()) {
                // TODO OTP
                String otp = Util.otpGeneration();
                user.setOtp(otp);
                userRepository.save(user);
                smsService.sendOtp(user.getUsername(), otp);
                return ResponseEntity
                        .ok(new ApiResponseModel(HttpStatus.BAD_REQUEST.value(), false, "User Not Verified"));
            }
            user.setPinCode(pinCodeRequest.getPinCode());

            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponseModel(HttpStatus.OK.value(), true, "PinCode Successfully Set"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponseModel(HttpStatus.BAD_REQUEST.value(), false, "PinCode Error Set"));
        }
    }


    public ResponseEntity<?> loginPinCode(PinCodeRequest pinCodeRequest, HttpServletRequest request, String language) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAgent currentUserAgent = userAgentRepository.findByUserAndUserAgent(user, request.getHeader("User-Agent"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        List<String> roles = user.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        if (!currentUserAgent.isDeletedStatus() && currentUserAgent.isVerified()) {
            if (pinCodeRequest.getPinCode() == user.getPinCode()) {
                if (roles.contains(ERole.ROLE_DOCTOR.name())) {
                    DoctorRegisterResponse doctorRegisterResponse = new DoctorRegisterResponse();
                    Doctor doctor = user.getDoctor();
                    doctorRegisterResponse.setIsOTPVerified(true);
                    doctorRegisterResponse.setToken(jwt);
                    doctorRegisterResponse.setFaceScan(doctor.getFaceScan());
                    doctorRegisterResponse.setDob(doctor.getDob());
                    doctorRegisterResponse.setFee(doctor.getFee());
                    doctorRegisterResponse.setFingerPrints(doctor.getFingerPrints());
                    doctorRegisterResponse.setFullName(doctor.getFullName());
                    doctorRegisterResponse.setEmail(doctor.getEmail());
                    doctorRegisterResponse.setGender(doctor.getGender());
                    doctorRegisterResponse.setMobileNumber(user.getUsername());

                    List<String> specialitiesAsString = new ArrayList<>();
                    List<DoctorSpecialities> specs = doctorSpecialitiesRepository.findByDoctorId(user.getId());
                    switch (language) {
                        case "us":
                            specs.forEach(spec -> {
                                specialitiesAsString.add(spec.getEnglishName());
                            });
                            break;
                        case "ru":
                            specs.forEach(spec -> {
                                specialitiesAsString.add(spec.getRussianName());
                            });
                            break;
                        case "uz":
                            specs.forEach(spec -> {
                                specialitiesAsString.add(spec.getUzbekName());
                            });
                            break;
                    }
                    doctorRegisterResponse.setSpeciality(specialitiesAsString);
                    doctorRegisterResponse.setActivityType(doctor.getActivityType());

                    doctorRegisterResponse.setLicence(doctor.getLicence());
                    doctorRegisterResponse.setAboutSelf(doctor.getAboutSelf());
                    doctorRegisterResponse.setExperience(doctor.getExperience());
                    doctorRegisterResponse.setIsActive(doctor.getIsActive());
                    doctorRegisterResponse.setCertificate(doctor.getCertificate());
                    doctorRegisterResponse.setSsn(doctor.getSsn());
                    doctorRegisterResponse.setCreatedBy(doctor.getCreatedBy());
                    doctorRegisterResponse.setStreet1(doctor.getAddress().getStreet1());
                    doctorRegisterResponse.setStreet2(doctor.getAddress().getStreet2());
                    doctorRegisterResponse.setCity(doctor.getAddress().getCity());
                    doctorRegisterResponse.setZipCode(doctor.getAddress().getZipCode());
                    doctorRegisterResponse.setState(doctor.getAddress().getState());
                    doctorRegisterResponse.setCountry(doctor.getAddress().getCountry());
                    doctorRegisterResponse.setLatitude(doctor.getLatitude());
                    doctorRegisterResponse.setLongitude(doctor.getLongitude());
                    doctorRegisterResponse.setId(user.getId());
                    return ResponseEntity.ok(doctorRegisterResponse);
                } else {
                    PatientRegisterResponse patientRegisterResponse = new PatientRegisterResponse();
                    patientRegisterResponse.setIsOTPVerified(true);
                    patientRegisterResponse.setToken(jwt);
                    Patient patient = user.getPatient();
                    patientRegisterResponse.setFaceScan(patient.getFaceScan());
                    patientRegisterResponse.setDob(patient.getDob());
                    patientRegisterResponse.setFingerPrints(patient.getFingerPrints());
                    patientRegisterResponse.setFullName(patient.getFullName());
                    patientRegisterResponse.setGender(patient.getGender());
                    patientRegisterResponse.setMobileNumber(user.getUsername());
                    patientRegisterResponse.setId(user.getId());
                    return ResponseEntity.ok(patientRegisterResponse);
                }
            }
            return ResponseEntity.status(400).body(new UniversalErrorResponseDTO(400, "The PIN code is incorrect"));
        }
        // TODO Login page
        return null;
    }


    public ResponseEntity<?> otpResend(OtpValidationRequest otpValidationRequest, HttpServletRequest request) {
        User user = userRepository.findByUsername(otpValidationRequest.getMobileNumber()).get();
        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(otpValidationRequest.getMobileNumber(), otpValidationRequest.getPassword())
//			);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
//            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            UserAgent userAgent = userAgentRepository.findByUserAndUserAgent(user, request.getHeader("User-Agent"));

            if (userAgent == null) {
                return ResponseEntity.badRequest().body(new PatientRegisterResponse("There is no such device"));
            }
            if (userAgent.isVerified()) {
                return ResponseEntity.badRequest().body(new PatientRegisterResponse("This device is already verified"));
            } else {
                String otp = Util.otpGeneration();
                boolean isOtpSent = smsService.sendOtpSuccess(otpValidationRequest.getMobileNumber(), otp);
                if (isOtpSent) {
                    user.setOtp(otp);
                    userRepository.save(user);
                    return ResponseEntity.ok(new SuccessResponseModel<>(200, "OTP sent successfully"));
                } else {
                    return ResponseEntity.status(500).body(new UniversalErrorResponseDTO(400, "Error in sending OTP"));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new UniversalErrorResponseDTO(400, "User Authentication failed"));
        }
    }


    public ResponseEntity<?> uploadPhoto(MultipartFile imageFile, HttpServletRequest request) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            final String code = user.getUsername();
            String logoName = code + ".png";
            String folder = "patient_images";
            if (user.hasRole(ERole.ROLE_DOCTOR)) {
                folder = "doctor_images";
            }

            String separator = System.getProperty("file.separator");

            File classpath = ResourceUtils.getFile("classpath:");
            File newFile = new File(fileLocation.replaceAll("classpath:", classpath.getAbsolutePath()) + separator
                    + "images" + separator + folder + separator + logoName);
            InputStream inputStream = imageFile.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, newFile);
            inputStream.close();
            HashMap<String, String> response = new HashMap<>();
            response.put("imageUrl", "/content/images/" + folder + "/" + logoName);
            user.setProfileImage(response.get("imageUrl"));
            userRepository.save(user);
            return ResponseEntity.ok(new SuccessResponseModel<>(200, response));
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("error" + e.getMessage());
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(new UniversalErrorResponseDTO(400, "Error in saving image"));
        }
    }


    public ResponseEntity<?> logout(AuthenticationRequest authenticationRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<UserPlatform> userPlatformList = userPlatformRepository.findByFcmTokenOrVoipToken(
                authenticationRequest.getFcmToken(),
                authenticationRequest.getVoipToken() == null ? "" : authenticationRequest.getVoipToken());
        List<UserPlatform> deleteUserPlatformList = new ArrayList<>();

        for (UserPlatform userPlatform : userPlatformList) {
            if (userPlatform.getUserId().equals(user.getId())) {
                deleteUserPlatformList.add(userPlatform);
            }
        }
        userPlatformRepository.deleteAll(deleteUserPlatformList);

        return ResponseEntity.ok(new DoctorRegisterResponse("User Successfully Logged out."));
    }


    public ResPageable getBalanceAndTransactions(int page, int offset) {
        Pageable pageable = PageRequest.of(page - 1, offset, (Sort.by("date")).descending());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LedgerAccount ledgerAccount = ledgerAccountService.getAccountByUserId(user.getId());
        Page<Transaction> transactionPage = transactionService.getTransactions(ledgerAccount, pageable);

        TransactionsResponse transactionsResponse = new TransactionsResponse();

        if (page == 1) {
            double twoWeekBalance = 0;

            LocalDateTime today = LocalDateTime.now();
            LocalDateTime ld2 = today.minusDays(14);
            List<Transaction> transactionList = transactionService.getTransactionsForTwoWeeks(ledgerAccount, ld2);

            for (Transaction transaction : transactionList) {
                if (transaction.getDate().compareTo(ld2) >= 0) {
                    if (transaction.getType() == Type.CREDIT) {

                        twoWeekBalance = twoWeekBalance + transaction.getAmount();
                    } else {
                        twoWeekBalance = twoWeekBalance - transaction.getAmount();
                    }
                }
            }
            transactionsResponse.setWithdrawableBalance(ledgerAccount.getBalance() - twoWeekBalance);
            transactionsResponse.setBalance(ledgerAccount.getBalance());

        }

        // TransactionsResponse transactionsResponse = new TransactionsResponse();
        transactionsResponse.setTransactions(transactionPage.getContent().stream().map(e -> {
            e.setAccount(null);
            return e;
        }).collect(Collectors.toList()));
//		return ResponseEntity.ok(transactionsResponse);

        return new ResPageable(
                transactionsResponse,
                transactionPage.getTotalElements(), transactionPage.getTotalPages(), page);
    }


    public ResponseEntity<?> getRefundTransactions(int page, int offset) {
        Pageable pageable = PageRequest.of(page - 1, offset, (Sort.by("date")).descending());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LedgerAccount ledgerAccount = ledgerAccountService.getAccountByUserId(user.getId());
        Page<Transaction> transactionRefund = transactionService.getRefundTransactions(ledgerAccount, pageable);
        TransactionsResponse transactionsResponse = new TransactionsResponse();

        transactionsResponse.setTransactions(transactionRefund.getContent().stream().map(e -> {
            e.setAccount(null);
            return e;
        }).collect(Collectors.toList()));

        return ResponseEntity.ok(new ResPageable(
                transactionsResponse,
                transactionRefund.getTotalElements(), transactionRefund.getTotalPages(), page));
    }


    public ResponseEntity<?> getDrugDoseUnits(BasicLanguageDTO basicLanguageDTO) {
        List<Map<String, String>> drugDosesAsString = new ArrayList<>();
        List<DrugDoseUnits> specs = drugDoseUnitsRepository.findAll();
        switch (basicLanguageDTO.getLanguage()) {
            case "us":
                specs.forEach(spec -> {
                    drugDosesAsString.add(ImmutableMap.of("value", spec.getCode(), "label", spec.getEnglishName()));
                });
                break;
            case "ru":
                specs.forEach(spec -> {
                    drugDosesAsString.add(ImmutableMap.of("value", spec.getCode(), "label", spec.getRussianName()));
                });
                break;
            case "uz":
                specs.forEach(spec -> {
                    drugDosesAsString.add(ImmutableMap.of("value", spec.getCode(), "label", spec.getUzbekName()));
                });
                break;
        }

        return ResponseEntity.ok(new SuccessResponseModel<>(200, drugDosesAsString));
    }

    public ResponseEntity<?> deleteDocument(Long documentId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getDoctor() != null) {
            Optional<DoctorDocument> doctorDocument = doctorDocumentRepository.findById(documentId);
            if (!doctorDocument.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new UniversalErrorResponseDTO(400, "Document is not available with given ID"));
            }
            if (!doctorDocument.get().getDoctorId().equals(user.getId())) {
                return ResponseEntity.badRequest()
                        .body(new UniversalErrorResponseDTO(400, "You are not authrize to access this"));
            }
            doctorDocumentRepository.delete(doctorDocument.get());
        } else if (user.getPatient() != null) {
            Optional<PatientDocument> patientDocument = patientDocumentRepository.findById(documentId);
            if (!patientDocument.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new UniversalErrorResponseDTO(400, "Document is not available with given ID"));
            }
            if (!patientDocument.get().getPatientId().equals(user.getId())) {
                return ResponseEntity.badRequest()
                        .body(new UniversalErrorResponseDTO(400, "You are not authrize to access this"));
            }

            patientDocument.get().getDoctor().clear();
            patientDocumentRepository.delete(patientDocument.get());
        }
        return ResponseEntity.ok(new SuccessResponseModel<>(200, "Document deleted Successfully"));
    }
}*/
