package uz.tm.tashman.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.models.AuthenticationModel;
import uz.tm.tashman.models.BasicModel;
import uz.tm.tashman.services.UserService;
import uz.tm.tashman.util.HTTPUtil;

import javax.servlet.http.HttpServletRequest;

import static uz.tm.tashman.enums.StatusCodes.*;
import static uz.tm.tashman.util.Util.isBlank;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController extends HTTPUtil {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/otpVerification")
    public ResponseEntity<?> otpVerification(@RequestBody AuthenticationModel authenticationModel, HttpServletRequest header) {
        if (isBlank(authenticationModel.getMobileNumber())) {
            return BadRequestResponse(USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(authenticationModel.getOtp())) {
            return BadRequestResponse(USER_OTP_NOT_ENTERED);
        }
        if (isBlank(authenticationModel.getDeviceId())) {
            return BadRequestResponse(DEVICE_INFO_MISSING);
        }
        return userService.otpVerification(authenticationModel, header);
    }

    @PostMapping(value = "/otpResend")
    public ResponseEntity<?> otpResend(@RequestBody AuthenticationModel authenticationModel, HttpServletRequest header) {
        if (isBlank(authenticationModel.getMobileNumber())) {
            return BadRequestResponse(USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(authenticationModel.getPassword())) {
            return BadRequestResponse(USER_PASSWORD_NOT_ENTERED);
        }
        if (isBlank(authenticationModel.getDeviceId())) {
            return BadRequestResponse(DEVICE_INFO_MISSING);
        }
        return userService.otpResend(authenticationModel, header);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody AuthenticationModel authenticationModel, HttpServletRequest header) {
        if (isBlank(authenticationModel.getMobileNumber())) {
            return BadRequestResponse(USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(authenticationModel.getDeviceId())) {
            return BadRequestResponse(DEVICE_INFO_MISSING);
        }
        return userService.forgotPassword(authenticationModel, header);
    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody AuthenticationModel authenticationModel, HttpServletRequest header) {
        if (isBlank(authenticationModel.getMobileNumber())) {
            return BadRequestResponse(USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(authenticationModel.getPassword())) {
            return BadRequestResponse(USER_PASSWORD_NOT_ENTERED);
        }
        if (isBlank(authenticationModel.getOtp())) {
            return BadRequestResponse(USER_OTP_NOT_ENTERED);
        }
        if (isBlank(authenticationModel.getDeviceId())) {
            return BadRequestResponse(DEVICE_INFO_MISSING);
        }

        return userService.changePassword(authenticationModel, header);
    }

    @PostMapping(value = "/getGenderList")
    public ResponseEntity<?> getGenderList(@RequestBody BasicModel basicModel) {

        return userService.getGenderList(basicModel);
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