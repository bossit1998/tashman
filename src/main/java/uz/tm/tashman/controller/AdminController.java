package uz.tm.tashman.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.models.UserBlockOrDeleteModel;
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
        if (isBlank(userRequestModel.getUser())) {
            return BadRequestResponse(USER_DETAILS_ARE_MISSING);
        }
        if (isBlank(userRequestModel.getUserAgent())) {
            return BadRequestResponse(USER_AGENT_DETAILS_ARE_MISSING);
        }
        if (isBlank(userRequestModel.getUser().getMobileNumber())) {
            return BadRequestResponse(USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(userRequestModel.getUser().getName())) {
            return BadRequestResponse(USER_NAME_NOT_ENTERED);
        }
        if (isBlank(userRequestModel.getUser().getSurname())) {
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

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody UserBlockOrDeleteModel userBlockOrDeleteModel) {
        return adminService.deleteUser(userBlockOrDeleteModel);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/blockUser")
    public ResponseEntity<?> blockUser(@RequestBody UserBlockOrDeleteModel userBlockOrDeleteModel) {
        return adminService.blockUser(userBlockOrDeleteModel);
    }
}