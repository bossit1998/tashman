package uz.tm.tashman.models.responseModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterResponse {

    private Long id;
    private String mobileNumber;
    private String fullName;
    private String dob;
    private String gender;
    private String password;
    private String ipAddress;
    private String ssn;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Boolean isActive;
    private String createdBy;
    private String deletedBy;
    private String message;
    private Boolean isOTPVerified;
    private String fingerPrints;
    private String token;

    public RegisterResponse(String message) {
        this.message = message;
    }
}
