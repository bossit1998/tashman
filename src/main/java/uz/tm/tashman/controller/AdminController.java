package uz.tm.tashman.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.models.requestModels.AuthenticationRequestModel;
import uz.tm.tashman.models.requestModels.UserRequestModel;
import uz.tm.tashman.services.AdminService;
import uz.tm.tashman.util.HTTPUtil;

import javax.servlet.http.HttpServletRequest;

import static uz.tm.tashman.enums.StatusCodes.*;
import static uz.tm.tashman.util.Util.isBlank;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController extends HTTPUtil {

    final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserRequestModel userRequestModel, HttpServletRequest request) {
        if (isBlank(userRequestModel.getUser().getMobileNumber())) {
            return BadRequestResponse(USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(userRequestModel.getUser().getFullName())) {
            return BadRequestResponse(USER_NAME_NOT_ENTERED);
        }
        if (isBlank(userRequestModel.getUser().getDob())) {
            return BadRequestResponse(USER_DOB_NOT_ENTERED);
        }
        if (isBlank(userRequestModel.getUser().getGender())) {
            return BadRequestResponse(USER_GENDER_NOT_ENTERED);
        }
        if (isBlank(userRequestModel.getUser().getPassword())) {
            return BadRequestResponse(USER_PASSWORD_NOT_ENTERED);
        }
        return adminService.registration(userRequestModel, request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestModel authenticationRequestModel, HttpServletRequest request) {
        if (isBlank(authenticationRequestModel.getAuthentication().getMobileNumber())) {
            return BadRequestResponse(USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(authenticationRequestModel.getAuthentication().getPassword())) {
            return BadRequestResponse(USER_PASSWORD_NOT_ENTERED);
        }
        if (isBlank(authenticationRequestModel.getAuthentication().getDeviceId()) && isBlank(authenticationRequestModel.getUserAgent())) {
            return BadRequestResponse(DEVICE_INFO_MISSING);
        }
        return adminService.login(authenticationRequestModel, request);
    }
}