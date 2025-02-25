package uz.tm.tashman.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uz.tm.tashman.enums.Language;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.wrapperModels.ErrorResponse;
import uz.tm.tashman.models.wrapperModels.SuccessResponse;
import uz.tm.tashman.services.LogService;

import java.util.Arrays;

public class HTTPUtil {
    @Autowired
    LogService logService;

    public ResponseEntity<?> OkResponse(StatusCodes status) {
        Language language = Util.getLanguageFromAuthentication();
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(status, language, null));
    }

    public ResponseEntity<?> OkResponse(StatusCodes status, Object data) {
        Language language = Util.getLanguageFromAuthentication();
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(status, language, data));
    }

    public ResponseEntity<?> BadRequestResponse(StatusCodes status) {
        Language language = Util.getLanguageFromAuthentication();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(status, language));
    }

    public ResponseEntity<?> UnauthorizedResponse(StatusCodes status) {
        Language language = Util.getLanguageFromAuthentication();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(status, language));
    }

    public ResponseEntity<?> InternalServerErrorResponse(Object error) {
        Language language = Util.getLanguageFromAuthentication();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(StatusCodes.INTERNAL_ERROR, language, error));
    }

    public ResponseEntity<?> InternalServerErrorResponse(Exception e) {
        System.out.println(e);
        logService.saveToLog(e);
        Language language = Util.getLanguageFromAuthentication();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(StatusCodes.INTERNAL_ERROR, language, exceptionAsString(e)));
    }

    public String exceptionAsString(Exception e) {
        return "Exception: \n "
                + "message: " + e.getMessage() + " \n "
                + "cause: " + e.getCause() + " \n "
                + "stacktrace: " + Arrays.toString(e.getStackTrace());
    }
}