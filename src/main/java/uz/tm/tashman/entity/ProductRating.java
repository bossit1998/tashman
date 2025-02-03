package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ratingFor; //?
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private String userNickname;
    private String review;
    private Double rating;

    private LocalDateTime createdDate;
    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;
    private Long deletedBy;
    private LocalDateTime deletedDate;
}