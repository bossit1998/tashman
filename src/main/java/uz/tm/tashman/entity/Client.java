package uz.tm.tashman.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -8723441071526027773L;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "dob")
    private String dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "status")
    private String status;

    @Column(name = "finger_prints")
    private String fingerPrints;

    @Column(name = "face_scan")
    private String faceScan;

    @Column(name = "qr_code")
    private String qrCode;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Override
    public Long getId() {
        return null;
    }
}