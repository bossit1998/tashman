package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserRegisterResponse {
    private String message;
    private String token;
    private String email;
    private String mobileNumber;
    private Boolean isOTPVerified;
    private String fullName;
    private String dob;
    private String gender;
    private String faceScan;
    private String fingerPrints;
    private Long id;
    private String profileImageUrl;

    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Double latitude;
    private Double longitude;

    private Boolean isActive;
}
