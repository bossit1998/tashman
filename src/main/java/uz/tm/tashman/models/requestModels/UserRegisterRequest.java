package uz.tm.tashman.models.requestModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    private String mobileNumber;
    private String fullName;
    private String dob;
    private String gender;
    private String email;
    private String password;
    private String faceScan;
    private String fingerPrints;
    private String fcmToken;
    private String platform;
    private String voipToken;

    private String street;
    private String district;
    private String city;
    private String region;
    private String country;
    private String zipCode;
    private Double latitude;
    private Double longitude;

    private Boolean isActive;


    public UserRegisterRequest(String mobileNumber, String fullName, String dob, String gender, String email, String password, String faceScan, String fingerPrints, String fcmToken, String platform, String voipToken, Boolean isActive) {
        this.mobileNumber = mobileNumber;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.faceScan = faceScan;
        this.fingerPrints = fingerPrints;
        this.fcmToken = fcmToken;
        this.platform = platform;
        this.voipToken = voipToken;
        this.isActive = isActive;
    }
}
