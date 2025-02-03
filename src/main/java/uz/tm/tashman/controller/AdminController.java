package uz.tm.tashman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.models.AuthenticationModel;
import uz.tm.tashman.models.UserModel;
import uz.tm.tashman.services.AdminService;
import uz.tm.tashman.util.HTTPUtil;

import javax.servlet.http.HttpServletRequest;

import static uz.tm.tashman.enums.StatusCodes.*;
import static uz.tm.tashman.util.Util.isBlank;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController extends HTTPUtil {

    @Autowired
    AdminService adminService;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserModel userModel, HttpServletRequest request) {
        if (isBlank(userModel.getMobileNumber())) {
            return BadRequestResponse(USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(userModel.getFullName())) {
            return BadRequestResponse(USER_NAME_NOT_ENTERED);
        }
        if (isBlank(userModel.getDob())) {
            return BadRequestResponse(USER_DOB_NOT_ENTERED);
        }
        if (isBlank(userModel.getGender())) {
            return BadRequestResponse(USER_GENDER_NOT_ENTERED);
        }
        if (isBlank(userModel.getPassword())) {
            return BadRequestResponse(USER_PASSWORD_NOT_ENTERED);
        }
        return adminService.registration(userModel, request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationModel authenticationModel, HttpServletRequest request) {
        if (isBlank(authenticationModel.getMobileNumber())) {
            return BadRequestResponse(USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(authenticationModel.getPassword())) {
            return BadRequestResponse(USER_PASSWORD_NOT_ENTERED);
        }
        if (isBlank(authenticationModel.getDeviceId()) && isBlank(authenticationModel.getUserAgentModel())) {
            return BadRequestResponse(DEVICE_INFO_MISSING);
        }
        return adminService.login(authenticationModel, request);
    }
}