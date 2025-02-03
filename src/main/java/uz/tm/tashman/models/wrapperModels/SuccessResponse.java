package uz.tm.tashman.models.wrapperModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uz.tm.tashman.enums.Language;
import uz.tm.tashman.enums.StatusCodes;

@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SuccessResponse<T> {
    private Integer status;
    private String message;
    private T data;

    public SuccessResponse(StatusCodes status, Language language, T data) {
        this.status = status.getId();
        this.data = data;
        this.message = StatusCodes.getNameByLanguage(status, language);
    }
}