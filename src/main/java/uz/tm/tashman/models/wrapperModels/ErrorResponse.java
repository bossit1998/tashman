package uz.tm.tashman.models.wrapperModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.tm.tashman.enums.Language;
import uz.tm.tashman.enums.StatusCodes;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private Integer status;
    private String message;
    private Object error;

    public ErrorResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(StatusCodes status, Language language) {
        this.status = status.getId();
        this.message = StatusCodes.getNameByLanguage(status, language);
    }

    public ErrorResponse(StatusCodes status, Language language, Object error) {
        this.status = status.getId();
        this.message = StatusCodes.getNameByLanguage(status, language);
        this.error = error;
    }
}