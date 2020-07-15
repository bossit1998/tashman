package uz.tm.tashman.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpModel extends SignInModel{
    private String fullName;
    private String email;
    private String phoneNumber;
    private String region;
    private String companyName;
    private String privilege;
}
