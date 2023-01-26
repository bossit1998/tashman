package uz.tm.tashman.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Gender;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Client implements Serializable {

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    private String name;
    private String surname;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String email;

    private String fingerPrints;

    private String faceScan;

    private String qrCode;

    private LocalDateTime createdDate;

    public String getFullName() {
        return name + " " + surname;
    }
}