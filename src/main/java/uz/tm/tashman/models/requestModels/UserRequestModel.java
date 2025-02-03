package uz.tm.tashman.models.requestModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.models.UserAgentModel;
import uz.tm.tashman.models.UserModel;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestModel {
    private UserModel user;
    private UserAgentModel userAgent;
}
