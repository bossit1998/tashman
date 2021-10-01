package uz.tm.tashman.models.requestModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    private String mobileNumber;
    private String password;
    private String otp;
    private String fcmToken;
    private String platform;
    private String voipToken;
}