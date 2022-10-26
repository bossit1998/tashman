package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationModel {
    private String encodedId;
    private String deviceId;
    private String mobileNumber;
    private String password;
    private String otp;
    private Integer pinCode;
    private String fcmToken;
    private String platform;
    private String voipToken;

    private UserAgentModel userAgentModel;
}