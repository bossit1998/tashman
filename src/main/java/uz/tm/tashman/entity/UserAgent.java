package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Platform;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserAgent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String encodedId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String otp;

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
    private String hostname;

    @Column(columnDefinition = "boolean default false")
    private boolean isVerified = false;

    private LocalDateTime tokenDate;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    private Long deletedBy;

    private LocalDateTime deletedDate;

    private String fcmToken;
    private String voipToken;

    @Enumerated(EnumType.STRING)
    private Platform platform;
}