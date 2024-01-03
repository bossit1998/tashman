package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long belongsToId; //userId, producerId, etc
    private String belongsToType; //user, producer, etc.
    private String street;
    private String district;
    private String city;
    private String region;
    private String country;
    private Double latitude;
    private Double longitude;
    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;
    private LocalDateTime deletedDate;
}