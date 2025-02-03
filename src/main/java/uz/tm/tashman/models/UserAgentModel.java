package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Platform;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAgentModel implements Serializable {
    private Long id;
    private String userAgent;
    private String ip;
    private String referer;
    private String fullURL;
    private String clientOS;
    private String browser;
    private String appVersion;
    private String hardwareInfo;
    private String city;
    private String region;
    private String country;
    private String location;
    private String org;
    private String timezone;
    private String readme;
    private Boolean isVerified;
    private LocalDateTime tokenDate;
    private Boolean isDeleted;
    private LocalDateTime deletedDate;
    private String fcmToken;
    private String voipToken;
    private Platform platform;
}