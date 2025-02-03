package uz.tm.tashman.models.wrapperModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.util.Util;

@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SuccessResponse<T> {
    private Integer status;
    private String message;
    private T data;

    public SuccessResponse(StatusCodes status, String language, T data) {
        this.status = status.getId();
        this.data = data;
        this.message = StatusCodes.getNameByLanguage(status, language);
    }
}