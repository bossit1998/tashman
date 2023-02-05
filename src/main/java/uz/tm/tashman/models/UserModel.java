package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Language;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel {
        private Long id;
        private String mobileNumber;
        private String name;
        private String surname;
        private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        private LocalDate dob;
        private String gender;
        private String password;
        private Boolean isActive;
        private Boolean isDeleted;
        private String createdBy;
        private String deletedBy;
        private String message;
        private String deviceId;
        private Boolean isOTPVerified;
        private String fingerPrints;
        private String token;
        private String email;
        private String faceScan;
        private String profileImageUrl;
        private Language language;
        private String role;

    public void clear() {
        this.id = null;
        this.mobileNumber = null;
        this.fullName = null;
        this.dob = null;
        this.gender = null;
        this.password = null;
        this.isActive = null;
        this.isDeleted = null;
        this.createdBy = null;
        this.deletedBy = null;
        this.message = null;
        this.isOTPVerified = null;
        this.fingerPrints = null;
        this.token = null;
        this.email = null;
        this.faceScan = null;
        this.profileImageUrl = null;
        this.language = null;
        this.role = null;
    }
}