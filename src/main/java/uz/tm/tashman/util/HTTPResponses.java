package uz.tm.tashman.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uz.tm.tashman.models.wrapModels.ErrorResponse;
import uz.tm.tashman.models.wrapModels.SuccessResponse;

public class HTTPResponses {

    public ResponseEntity<?> OkResponse(String message, Object data) {
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(200, message, data));
    }

    public ResponseEntity<?> CreatedResponse(String message, Object object) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(201, message + " created successfully", object));
    }

    public ResponseEntity<?> EmptyResponse(String message) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ErrorResponse(204, message + " does'nt exist"));
    }

    public ResponseEntity<?> BadRequestResponse(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, message + " not entered!"));
    }

    public ResponseEntity<?> ForbiddenResponse() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, "You don't have access to this resource"));
    }

    public ResponseEntity<?> InternalServerErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, message));
    }

    public ResponseEntity<?> NotImplementedResponse() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ErrorResponse(501, "This function is not implemented yet"));
    }
}