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

    public ResponseEntity<?> AlreadyExistsResponse(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, message + " already exists!"));
    }

    public ResponseEntity<?> WrongDataResponse(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, message + " is wrong!"));
    }

    public ResponseEntity<?> UnauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, "You don't have access to this resource"));
    }

    public ResponseEntity<?> ForbiddenResponse(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, message));
    }

    public ResponseEntity<?> InternalServerErrorResponse(String message, Object error) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, message, error));
    }

    public ResponseEntity<?> NotImplementedResponse() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ErrorResponse(501, "This function is not implemented yet"));
    }
}