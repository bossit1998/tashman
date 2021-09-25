package uz.tm.tashman.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.tm.tashman.enums.Platform;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user_platform")
@AllArgsConstructor
@NoArgsConstructor
public class UserPlatform implements Serializable {

    private static final long serialVersionUID = 7845739329016579049L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private String fcmToken;

    private String voipToken;

    @Enumerated(EnumType.STRING)
    private Platform platform;
}