package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IpInfoModel {
    Boolean status;
    String ip;
    String hostname;
    String city;
    String location;
    String region;
    String country;
    String loc;
    String org;
    String timezone;
    String readme;
    String data;
    String message;
}