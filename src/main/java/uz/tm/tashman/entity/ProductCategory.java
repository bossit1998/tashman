package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Language;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;
    private String nameEn;
    private String nameRu;
    private String nameUz;

    private String definition;
    private String imageUrl;

    @Column(columnDefinition = "boolean default false")
    private Boolean isVisible = false;
    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;
    @Column(columnDefinition = "boolean default false")
    private Boolean isInProduction = false;

    private LocalDateTime createdDate = LocalDateTime.now();
    private Long createdBy;
    private LocalDateTime deletedDate;
    private Long deletedBy;


    public String getName(Language language) {
        switch (language) {
            case EN:
                return nameEn;
            case UZ:
                return nameUz;
            case RU:
            default:
                return nameRu;
        }
    }
}