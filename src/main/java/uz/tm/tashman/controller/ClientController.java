package uz.tm.tashman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.AuthenticationModel;
import uz.tm.tashman.models.UserModel;
import uz.tm.tashman.services.ClientService;
import uz.tm.tashman.util.HTTPUtil;

import javax.servlet.http.HttpServletRequest;

import static uz.tm.tashman.util.Util.isBlank;


@RestController
@RequestMapping("/client")
@CrossOrigin(origins = "*")
public class ClientController extends HTTPUtil {

    @Autowired
    ClientService userService;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserModel userModel, HttpServletRequest request) {
        if (isBlank(userModel.getMobileNumber())) {
            return BadRequestResponse(StatusCodes.USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(userModel.getFullName())) {
            return BadRequestResponse(StatusCodes.USER_NAME_NOT_ENTERED);
        }
        if (isBlank(userModel.getDob())) {
            return BadRequestResponse(StatusCodes.USER_DOB_NOT_ENTERED);
        }
        if (isBlank(userModel.getGender())) {
            return BadRequestResponse(StatusCodes.USER_GENDER_NOT_ENTERED);
        }
        if (isBlank(userModel.getPassword())) {
            return BadRequestResponse(StatusCodes.USER_PASSWORD_NOT_ENTERED);
        }
        return userService.registration(userModel, request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationModel authenticationModel, HttpServletRequest request) {
        if (isBlank(authenticationModel.getMobileNumber())) {
            return BadRequestResponse(StatusCodes.USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(authenticationModel.getPassword())) {
            return BadRequestResponse(StatusCodes.USER_PASSWORD_NOT_ENTERED);
        }
        return userService.login(authenticationModel, request);
    }

//	@PreAuthorize("hasRole('PATIENT')")
//	@RequestMapping(value = "/createAndUpdatePatientDocuments", method = RequestMethod.POST)
//	public ResponseEntity<?> singleFileUpload(@ModelAttribute PatientDocumentRequest patientDocumentRequest)
//			throws Exception {
//		if ((patientDocumentRequest.getFile() == null || patientDocumentRequest.getFile().getBytes().length == 0)
//				&& patientDocumentRequest.getPatientDocumentId() == null) {
//			return ResponseEntity.badRequest().body(new UniversalErrorResponseDTO(400, "File not selected"));
//		}
//		return patientService.uploadPatientDocument(patientDocumentRequest);
//	}
//
//	@PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
//	@RequestMapping(value = "/getPatientDocument", method = RequestMethod.POST)
//	public ResponseEntity<?> getPatientDocument(@RequestBody PatientDocumentRequest patientDocumentRequest)
//			throws Exception {
//		return patientService.getPatientDocument(patientDocumentRequest);
//	}
//
//	@PreAuthorize("hasRole('PATIENT')")
//	@RequestMapping(value = "/getPatientDocumentList", method = RequestMethod.GET)
//	public ResponseEntity<?> getPatientDocumentList() throws Exception {
//		return patientService.getPatientDocumentList();
//}
//
//	@PreAuthorize("hasRole('PATIENT')")
//	@RequestMapping(value = "/getOrUpdatePatientInformation", method = RequestMethod.POST)
//	public ResponseEntity<?> getOrUpdatePatientInformation(@RequestBody PatientRegisterRequest patientRegisterRequest)
//			throws Exception {
//		return patientService.getOrUpdatePatientInformation(patientRegisterRequest);
//	}


//	@PreAuthorize("hasAnyRole('PATIENT', 'MONITORING')")
//	@GetMapping("")
//	public ResponseEntity<?> getProfile() {
//		logger.info("Starting of the Getting Patient Profile method of Patient Controller");
//		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return patientService.getProfile(user.getUsername());
//	}

//	@PreAuthorize("hasAnyRole('ADMIN', 'MAIN_DOCTOR', 'DOCTOR')")
//	@GetMapping("/{mobileNumber}")
//	public ResponseEntity<?> getProfile(@PathVariable String mobileNumber) {
//		logger.info("Starting of the Patient Profile method of Patient Controller for mobileNumber: {}", mobileNumber);
//		return patientService.getProfile(mobileNumber);
//	}
//
//	@PreAuthorize("hasAnyRole('ADMIN', 'MAIN_DOCTOR', 'DOCTOR')")
//	@PutMapping("/{mobileNumber}")
//	public ResponseEntity<?> updateProfile(@PathVariable String mobileNumber,
//                                           @RequestBody PatientRegisterRequest patientRegisterRequest) {
//		logger.info("Starting of the update Patient Profile method of Patient Controller");
//		return patientService.updateProfile(mobileNumber, patientRegisterRequest);
//	}

//	@PreAuthorize("hasRole('PATIENT')")
//	@PostMapping("/leaveFeedback")
//	public ResponseEntity<?> leaveFeedback(@RequestBody FeedbackDTO feedbackDTO, HttpServletRequest request) {
//		if (feedbackDTO.getDoctorId() == null) {
//			return ResponseEntity.badRequest().body(new UniversalErrorResponseDTO(400, "Doctor not chosen"));
//		}
//		if (feedbackDTO.getRating() == null) {
//			return ResponseEntity.badRequest().body(new UniversalErrorResponseDTO(400, "Rating not set"));
//		}
//		return patientService.leaveFeedback(feedbackDTO, request);
//	}
//
//	@PreAuthorize("hasRole('PATIENT')")
//	@PostMapping("/getDoctorFeedbacks")
//	public ResponseEntity<?> getDoctorFeedbacks(@RequestBody FeedbackDTO feedbackDTO, HttpServletRequest request) {
//		if (feedbackDTO.getDoctorId() == null) {
//			return ResponseEntity.badRequest().body(new UniversalErrorResponseDTO(400, "Doctor not chosen"));
//		}
//		return patientService.getDoctorFeedbacks(feedbackDTO);
//	}

}