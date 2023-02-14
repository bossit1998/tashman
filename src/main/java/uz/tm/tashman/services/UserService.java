package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import uz.tm.tashman.config.jwt.JwtUtils;
import uz.tm.tashman.entity.*;
import uz.tm.tashman.enums.ERole;
import uz.tm.tashman.enums.Gender;
import uz.tm.tashman.enums.Language;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.*;
import uz.tm.tashman.models.requestModels.AuthenticationRequestModel;
import uz.tm.tashman.repository.RoleRepository;
import uz.tm.tashman.repository.UserRepository;
import uz.tm.tashman.util.AES;
import uz.tm.tashman.util.HTTPUtil;
import uz.tm.tashman.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static uz.tm.tashman.enums.StatusCodes.*;
import static uz.tm.tashman.util.CONSTANT.*;
import static uz.tm.tashman.util.Util.isBlank;

@Service
public class UserService extends HTTPUtil {
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserAgentService userAgentService;
    private final LogService logService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private AdminService adminService;
    @Autowired
    private ClientService clientService;

    public UserService(PasswordEncoder encoder,
                       JwtUtils jwtUtils,
                       UserAgentService userAgentService,
                       LogService logService,
                       UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userAgentService = userAgentService;
        this.logService = logService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User getUserByUsername(String username) {
        if (username == null) {
            return null;
        }

        String encryptedUsername = AES.encrypt(username);
        if (encryptedUsername == null) {
            return null;
        }

        Optional<User> optUser = userRepository.findByUsername(encryptedUsername);
        return optUser.orElse(null);
    }

    public ResponseModel<User> createUser(ERole eRole, UserModel userModel, UserAgentModel userAgentModel, HttpServletRequest header) {
        ResponseModel<User> responseModel = new ResponseModel<>();

        try {
            String maskedPhoneNumber = Util.maskPhoneNumber(userModel.getMobileNumber());
            String username = AES.encrypt(userModel.getMobileNumber());
            String password = encoder.encode(userModel.getPassword());

            User user = new User(username, password, maskedPhoneNumber);

            Optional<Role> optRole = roleRepository.findByName(eRole);
            Role role = new Role(ERole.ROLE_CLIENT);
            if (optRole.isPresent()) {
                role = optRole.get();
            }
            user.setRoles(new HashSet<>(Collections.singleton(role)));

            user.setLanguage(Util.getLanguageFromAuthentication(userModel));

            user.setIsDeleted(false);
            user.setIsActive(false);
            user.setCreatedDate(LocalDateTime.now());

            if (eRole.equals(ERole.ROLE_CLIENT)) {
                Client client = clientService.createClient(userModel, user);
                user.setClient(client);
            } else if (eRole.equals(ERole.ROLE_ADMIN)) {
                Admin admin = adminService.createAdmin(userModel, user);
                user.setAdmin(admin);
            }

            user = userRepository.save(user);

            user.setEncodedId(Util.encodeId(user.getId()));

            userRepository.save(user);

            UserAgent userAgent = userAgentService.saveUserAgentInfo(user, userAgentModel, header);

            responseModel.setSuccess(true);
            responseModel.setDeviceId(userAgent.getEncodedId());
            responseModel.setData(user);
        } catch (Exception e) {
            logService.saveToLog(exceptionAsString(e));

            responseModel.setSuccess(false);
            responseModel.setException(e);
        }
        return responseModel;
    }

    public User getUserById(Long id) {
        if (isBlank(id)) {
            return null;
        }

        Optional<User> optUser = userRepository.findById(id);

        return optUser.orElse(null);
    }

    public User getUserByEncodedId(String encodedId) {
        if (isBlank(encodedId)) {
            return null;
        }

        Optional<User> optUser = userRepository.findByEncodedId(encodedId);

        return optUser.orElse(null);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(AES.encrypt(username));
    }

    //converterFunctions ->    Entity -> DTO
    public UserModel fromUserToUserModel(User user) {
        UserModel userModel = new UserModel();

        userModel.setId(user.getId());
        userModel.setMobileNumber(user.getMaskedPhoneNumber());
        userModel.setIsActive(user.getIsActive());
        userModel.setIsDeleted(user.getIsDeleted());
        userModel.setLanguage(user.getLanguage());
        userModel.setProfileImageUrl(getProfileImage(user));

        if (user.hasRole(ERole.ROLE_CLIENT)) {
            userModel = clientService.getClient(userModel, user);
        } else {
            userModel = adminService.getAdmin(userModel, user);
        }

        return userModel;
    }

    public String getProfileImage(User user) {
        String profileImage = BASE_URL + USER_DEFAULT_IMAGE_URL;
        if (user != null) {
            if (user.getProfileImageUrl() != null) {
                profileImage = user.getProfileImageUrl().replaceAll(CONTENT, BASE_URL);
            }
        }
        return profileImage;
    }

    public ResponseEntity<?> otpVerification(AuthenticationRequestModel authenticationRequestModel, HttpServletRequest header) {
        try {
            AuthenticationModel authenticationModel = authenticationRequestModel.getAuthentication();

            User user = getUserByUsername(authenticationModel.getMobileNumber());

            if (user == null) {
                return OkResponse(StatusCodes.USER_NOT_FOUND);
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(header));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            boolean userAgentExists = userAgentService.checkIfUserAgentExists(authenticationModel.getDeviceId(), user);

            if (userAgentExists) {
                UserAgent userAgent = userAgentService.getUserAgentByEncodedIdAndUserAndDeletedFalse(authenticationModel.getDeviceId(), user);

                if (userAgent.isVerified()) {
                    return OkResponse(DEVICE_ALREADY_VERIFIED);
                }

                if (authenticationModel.getOtp().equals(userAgent.getOtp())) {
                    Boolean userAgentVerified = userAgentService.verifyUserAgent(userAgent);

                    if (userAgentVerified) {
                        UserModel userModel = fromUserToUserModel(user);

                        userModel.setToken(jwt);
                        userModel.setIsOTPVerified(true);
                        userModel.setMessage(getNameByLanguage(USER_LOGGED_IN, user.getLanguage()));

                        return OkResponse(USER_LOGGED_IN, userModel);
                    } else {
                        return OkResponse(USER_OTP_NOT_VERIFIED);
                    }
                } else {
                    return OkResponse(USER_WRONG_OTP);
                }
            } else {
                return OkResponse(NO_SUCH_DEVICE);
            }
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> otpResend(AuthenticationRequestModel authenticationRequestModel, HttpServletRequest header) {
        try {
            AuthenticationModel authenticationModel = authenticationRequestModel.getAuthentication();

            User user = getUserByUsername(authenticationModel.getMobileNumber());

            if (user == null) {
                return OkResponse(StatusCodes.USER_NOT_FOUND);
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                    null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserModel userModel = new UserModel();

            boolean userAgentExists = userAgentService.checkIfUserAgentExists(authenticationModel.getDeviceId(), user);

            if (userAgentExists) {
                UserAgent userAgent = userAgentService.getUserAgentByEncodedIdAndUserAndDeletedFalse(authenticationModel.getDeviceId(), user);

                userAgent.setOtp(Util.generateOtp());
                userAgent.setTokenDate(LocalDateTime.now());
                userAgentService.save(userAgent);

                userModel.setIsOTPVerified(false);
                userModel.setMobileNumber(user.getMaskedPhoneNumber());
                userModel.setMessage(getNameByLanguage(OTP_SENT, user.getLanguage()));
            } else {
                return OkResponse(NO_SUCH_DEVICE);
            }
            return OkResponse(SUCCESS, userModel);
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> forgotPassword(AuthenticationRequestModel authenticationRequestModel, HttpServletRequest header) {
        try {
            AuthenticationModel authenticationModel = authenticationRequestModel.getAuthentication();
            UserAgentModel userAgentModel = authenticationRequestModel.getUserAgent();

            User user = getUserByUsername(authenticationModel.getMobileNumber());

            if (user == null) {
                return OkResponse(StatusCodes.USER_NOT_FOUND);
            }

            UserModel userModel = new UserModel();

            boolean userAgentExists = userAgentService.checkIfUserAgentExists(authenticationModel.getDeviceId(), user);

            UserAgent userAgent;

            if (userAgentExists) {
                userAgent = userAgentService.getUserAgentByEncodedIdAndUserAndDeletedFalse(authenticationModel.getDeviceId(), user);

                userAgent.setOtp(Util.generateOtp());
                userAgent.setTokenDate(LocalDateTime.now());
                userAgentService.save(userAgent);

                userModel.setMessage(getNameByLanguage(OTP_SENT, user.getLanguage()));
            } else {
                userAgent = userAgentService.saveUserAgentInfo(user, userAgentModel, header);

                userModel.setDeviceId(userAgent.getEncodedId());
                userModel.setMessage(getNameByLanguage(USER_OTP_NOT_VERIFIED, user.getLanguage()));
            }
            userModel.setIsOTPVerified(false);
            userModel.setMobileNumber(user.getMaskedPhoneNumber());

            return OkResponse(SUCCESS, userModel);
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> changePassword(AuthenticationRequestModel authenticationRequestModel, HttpServletRequest header) {
        try {
            AuthenticationModel authenticationModel = authenticationRequestModel.getAuthentication();
            UserAgentModel userAgentModel = authenticationRequestModel.getUserAgent();

            User user = getUserByUsername(authenticationModel.getMobileNumber());

            if (user == null) {
                return OkResponse(StatusCodes.USER_NOT_FOUND);
            }

            UserModel userModel = new UserModel();

            boolean userAgentExists = userAgentService.checkIfUserAgentExists(authenticationModel.getDeviceId(), user);

            UserAgent userAgent;

            if (userAgentExists) {
                userAgent = userAgentService.getUserAgentByEncodedIdAndUserAndDeletedFalse(authenticationModel.getDeviceId(), user);
            } else {
                userAgent = userAgentService.saveUserAgentInfo(user, userAgentModel, header);
            }

            if (authenticationModel.getOtp().equals(userAgent.getOtp())) {
                user.setPassword(encoder.encode(authenticationModel.getPassword()));
                userRepository.save(user);

                userModel.setIsOTPVerified(true);
                userModel.setMessage(getNameByLanguage(PASSWORD_CHANGED_SUCCESSFULLY, user.getLanguage()));
            } else {
                userModel.setIsOTPVerified(false);
                userModel.setMessage(getNameByLanguage(USER_WRONG_OTP, user.getLanguage()));
            }
            return OkResponse(SUCCESS, userModel);
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> getGenderList(BasicModel basicModel) {
        try {
            if (basicModel.getLanguage() == null) {
                basicModel.setLanguage(Language.RU);
            }

            List<HashMapModel> genderList = Gender.getAllByLanguage(basicModel.getLanguage());

            return OkResponse(SUCCESS, genderList);
        } catch (Exception e) {
            return InternalServerErrorResponse(e);
        }
    }

    public ResponseEntity<?> setPinCode(AuthenticationRequestModel authenticationRequestModel, HttpServletRequest request) {
        User client = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        client.setPinCode(authenticationRequestModel.getAuthentication().getPinCode());
        userRepository.save(client);
        return OkResponse(SUCCESS);
    }
    /*================================================================================================================*/

    /*
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

    */


    /*

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
*/
}