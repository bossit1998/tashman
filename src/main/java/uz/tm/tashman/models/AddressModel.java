package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.entity.User;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressModel {
    private Long id;
    private User user;
    private String street;
    private String district;
    private String city;
    private String region;
    private String country;
    private Double latitude;
    private Double longitude;
}