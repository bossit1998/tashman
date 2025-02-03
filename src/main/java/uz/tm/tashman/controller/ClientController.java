package uz.tm.tashman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.BasicModel;
import uz.tm.tashman.models.UserModel;
import uz.tm.tashman.models.requestModels.AuthenticationRequestModel;
import uz.tm.tashman.models.requestModels.UserRequestModel;
import uz.tm.tashman.services.ClientService;
import uz.tm.tashman.util.HTTPUtil;

import javax.servlet.http.HttpServletRequest;

import static uz.tm.tashman.enums.StatusCodes.*;
import static uz.tm.tashman.enums.StatusCodes.USER_NAME_NOT_ENTERED;
import static uz.tm.tashman.util.Util.isBlank;

@RestController
@RequestMapping("/client")
@CrossOrigin(origins = "*")
public class ClientController extends HTTPUtil {

    @Autowired
    ClientService clientService;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserRequestModel userRequestModel, HttpServletRequest request) {
        if (isBlank(userRequestModel.getUser())) {
            return BadRequestResponse(USER_DETAILS_ARE_MISSING);
        }
        if (isBlank(userRequestModel.getUserAgent())) {
            return BadRequestResponse(USER_AGENT_DETAILS_ARE_MISSING);
        }
        if (isBlank(userRequestModel.getUser().getMobileNumber())) {
            return BadRequestResponse(StatusCodes.USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(userRequestModel.getUser().getName())) {
            return BadRequestResponse(USER_NAME_NOT_ENTERED);
        }
        if (isBlank(userRequestModel.getUser().getSurname())) {
            return BadRequestResponse(USER_NAME_NOT_ENTERED);
        }
        if (isBlank(userRequestModel.getUser().getDob())) {
            return BadRequestResponse(StatusCodes.USER_DOB_NOT_ENTERED);
        }
        if (isBlank(userRequestModel.getUser().getGender())) {
            return BadRequestResponse(StatusCodes.USER_GENDER_NOT_ENTERED);
        }
        if (isBlank(userRequestModel.getUser().getPassword())) {
            return BadRequestResponse(StatusCodes.USER_PASSWORD_NOT_ENTERED);
        }
        return clientService.registration(userRequestModel, request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestModel authenticationRequestModel, HttpServletRequest request) {
        if (isBlank(authenticationRequestModel.getAuthentication().getMobileNumber())) {
            return BadRequestResponse(StatusCodes.USER_PHONE_NOT_ENTERED);
        }
        if (isBlank(authenticationRequestModel.getAuthentication().getPassword())) {
            return BadRequestResponse(StatusCodes.USER_PASSWORD_NOT_ENTERED);
        }
        return clientService.login(authenticationRequestModel, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @PostMapping(value = "/getProfile")
    public ResponseEntity<?> getProfile(@RequestBody BasicModel basicModel, HttpServletRequest httpRequestHeader) {
        return clientService.getProfile(basicModel, httpRequestHeader);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @PostMapping(value = "/editProfile")
    public ResponseEntity<?> editProfile(@RequestBody UserModel userModel, HttpServletRequest httpRequestHeader) {
        return clientService.editProfile(userModel, httpRequestHeader);
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