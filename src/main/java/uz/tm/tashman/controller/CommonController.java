package uz.tm.tashman.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.tm.tashman.util.HTTPResponses;

import uz.tm.tashman.models.*;
import uz.tm.tashman.models.requestModels.OtpValidationRequest;
import uz.tm.tashman.service.CommonService;
import uz.tm.tashman.util.BadRequestException;
import uz.tm.tashman.util.HTTPResponses;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/common")
@CrossOrigin(origins = "*")
public class CommonController extends HTTPResponses {
//    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    CommonService commonService;

/*
    @PostMapping("/forgotpassword")
    public ResponseEntity<?> forgotPassword(@RequestBody AuthenticationRequest authenticationRequest) {
        if (authenticationRequest.getMobileNumber() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponseModel(400, "Phone not entered"));
        }
        logger.info("Controller - forgotpassword API Called - Start");
        return commonService.forgotPassword(authenticationRequest);
    }
*/

/*
    @PostMapping(value = "/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody AuthenticationRequest authorizedRequest) {
        if (authorizedRequest.getMobileNumber() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponseModel(400, "Phone not entered"));
        }
        if (authorizedRequest.getOtp() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponseModel(400, "OTP not entered"));
        }
        if (authorizedRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponseModel(400, "Password not entered"));
        }
        logger.info("Controller - changepassword API Called - Start");
        return commonService.changePassword(authorizedRequest);
    }
*/

    @PostMapping(value = "/otp_validation")
    public ResponseEntity<?> otpValidation(@RequestBody OtpValidationRequest authorizedRequest,
                                           HttpServletRequest request) {
        if (authorizedRequest.getMobileNumber() == null) {
            return BadRequestResponse("Phone number");
        }
        if (authorizedRequest.getOtp() == null) {
            return BadRequestResponse("OTP");
        }
        return commonService.otpValidated(authorizedRequest, request);
    }

/*
    @PostMapping(value = "/otpResend")
    public ResponseEntity<?> otpResend(@RequestBody OtpValidationRequest otpValidationRequest,
                                       HttpServletRequest request) {
        if (otpValidationRequest.getMobileNumber() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponseModel(400, "Phone not entered"));
        }
        if (otpValidationRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponseModel(400, "Password not entered"));
        }
        logger.info("Controller - otpResend API Called - Start");
        return commonService.otpResend(otpValidationRequest, request);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout(@RequestBody AuthenticationRequest authenticationRequest) {
        return commonService.logout(authenticationRequest);
    }


    @PostMapping("/setPinCode")
    public ResponseEntity<?> setPinCode(@RequestBody PinCodeRequest pinCodeRequest, HttpServletRequest request) {
        if (pinCodeRequest.getPinCode() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponseModel(400, "Pin code not entered"));
        }
        return commonService.setPinCode(pinCodeRequest, request);
    }

    @PostMapping("/loginWithPinCode")
    public ResponseEntity<?> loginPinCode(@RequestBody PinCodeRequest pinCodeRequest, HttpServletRequest request) {
        if (pinCodeRequest.getPinCode() == null) {
            return ResponseEntity.badRequest().body(new ErrorResponseModel(400, "Pin code not entered"));
        }
        String language;
        if (request.getHeader("language") == null) {
            language = "us";
        } else {
            language = request.getHeader("language");
        }
        return commonService.loginPinCode(pinCodeRequest, request, language);
    }

    @PreAuthorize("hasRole('PATIENT') || hasRole('DOCTOR')")
    @PostMapping(value = "/uploadPhoto", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadPhoto(@RequestBody MultipartFile imageFile, HttpServletRequest request)
            throws IOException {
        if (imageFile == null || imageFile.getBytes().length == 0) {
            return ResponseEntity.badRequest().body(new ErrorResponseModel(400, "Image not chosen"));
        }
        return commonService.uploadPhoto(imageFile, request);
    }

    @GetMapping("/getDrugDoseUnits")
    public ResponseEntity<?> getDrugDoseUnits(HttpServletRequest request) {
        BasicLanguageDTO basicLanguageDTO = new BasicLanguageDTO("us");
        if (request.getHeader("language") != null) {
            if (request.getHeader("language").equals("ru")) {
                basicLanguageDTO.setLanguage("ru");
            } else if (request.getHeader("language").equals("uz")) {
                basicLanguageDTO.setLanguage("uz");
            }
        }
        return commonService.getDrugDoseUnits(basicLanguageDTO);

    }

    @GetMapping("/getBalanceAndTransactions")
    public HttpEntity<?> getBalanceAndTransactions(@RequestParam int page, @RequestParam int offset) {
        return ResponseEntity.ok(commonService.getBalanceAndTransactions(page, offset));
    }

    @GetMapping("/getRefundTransactions")
    public ResponseEntity<?> getRefundTransactions(@RequestParam int page, @RequestParam int offset) {
        return commonService.getRefundTransactions(page, offset);
    }

    @PreAuthorize("hasRole('PATIENT') || hasRole('DOCTOR')")
    @DeleteMapping("/deleteDocument/{documentId}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long documentId) {
        return commonService.deleteDocument(documentId);
    }
*/

}
