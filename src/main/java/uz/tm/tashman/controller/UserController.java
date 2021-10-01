package uz.tm.tashman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.util.HTTPResponses;
import uz.tm.tashman.models.requestModels.AuthenticationRequest;
import uz.tm.tashman.models.requestModels.UserRegisterRequest;
import uz.tm.tashman.service.UserService;

import javax.servlet.http.HttpServletRequest;

import static uz.tm.tashman.util.StringUtil.isBlank;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController extends HTTPResponses {

    @Autowired
    UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest userRegisterRequest,
                                          HttpServletRequest request) {
        if (isBlank(userRegisterRequest.getMobileNumber())) {
            return BadRequestResponse("Mobile number");
        }
        if (isBlank(userRegisterRequest.getFullName())) {
            return BadRequestResponse("Name");
        }
        if (isBlank(userRegisterRequest.getDob())) {
            return BadRequestResponse("Date of birth");
        }
        if (isBlank(userRegisterRequest.getGender())) {
            return BadRequestResponse("Gender");
        }
        if (isBlank(userRegisterRequest.getPassword())) {
            return BadRequestResponse("Password");
        }
        return userService.registration(userRegisterRequest, request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                       HttpServletRequest request) {
        if (isBlank(authenticationRequest.getMobileNumber())) {
            return BadRequestResponse("Mobile number");
        }
        if (isBlank(authenticationRequest.getPassword())) {
            return BadRequestResponse("Password");
        }
        return userService.login(authenticationRequest, request);
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