package uz.tm.tashman.models.requestModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.models.AuthenticationModel;
import uz.tm.tashman.models.UserAgentModel;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationRequestModel {
    private AuthenticationModel authentication;
    private UserAgentModel userAgent;
}
