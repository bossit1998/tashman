package uz.tm.tashman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.models.requestModels.AuthenticationRequest;
import uz.tm.tashman.models.requestModels.OtpValidationRequest;
import uz.tm.tashman.services.CommonService;
import uz.tm.tashman.util.HTTPResponses;

import javax.servlet.http.HttpServletRequest;

import static uz.tm.tashman.util.StringUtil.isBlank;


@RestController
@RequestMapping("/common")
@CrossOrigin(origins = "*")
public class CommonController extends HTTPResponses {
    //    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    CommonService commonService;

    @PostMapping("/forgotpassword")
    public ResponseEntity<?> forgotPassword(@RequestBody AuthenticationRequest authenticationRequest) {
        if (isBlank(authenticationRequest.getMobileNumber())) {
            return BadRequestResponse("Mobile number");
        }
        return commonService.forgotPassword(authenticationRequest);
    }

    @PostMapping(value = "/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody AuthenticationRequest authorizedRequest) {
        if (isBlank(authorizedRequest.getMobileNumber())) {
            return BadRequestResponse("Mobile number");
        }
        if (isBlank(authorizedRequest.getOtp())) {
            return BadRequestResponse("OTP");
        }
        if (isBlank(authorizedRequest.getPassword())) {
            return BadRequestResponse("Password");
        }
        return commonService.changePassword(authorizedRequest);
    }

    @PostMapping(value = "/otp_validation")
    public ResponseEntity<?> otpValidation(@RequestBody OtpValidationRequest authorizedRequest,
                                           HttpServletRequest request) {
        if (isBlank(authorizedRequest.getMobileNumber())) {
            return BadRequestResponse("Mobile number");
        }
        if (isBlank(authorizedRequest.getOtp())) {
            return BadRequestResponse("OTP");
        }
        return commonService.otpValidated(authorizedRequest, request);
    }


    @PostMapping(value = "/otpResend")
    public ResponseEntity<?> otpResend(@RequestBody OtpValidationRequest otpValidationRequest,
                                       HttpServletRequest request) {
        if (isBlank(otpValidationRequest.getMobileNumber())) {
            return BadRequestResponse("Mobile number");
        }
        if (isBlank(otpValidationRequest.getPassword())) {
            return BadRequestResponse("Password");
        }
        return commonService.otpResend(otpValidationRequest, request);
    }

    /*

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
