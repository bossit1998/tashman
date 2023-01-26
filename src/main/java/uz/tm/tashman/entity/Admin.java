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
public class Admin extends User implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    private String name;
    private String surname;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDateTime createdDate;

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}