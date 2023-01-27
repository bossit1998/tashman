package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Language;

import javax.persistence.*;
import java.time.LocalDateTime;

import static uz.tm.tashman.util.Util.checkLanguage;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;
    private String englishName;
    private String russianName;
    private String uzbekName;
    private String definition;
    private String imageUrl;

    private Long createdBy;
    private LocalDateTime createdDate;

    @Column(columnDefinition = "boolean default false")
    private Boolean isActive = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    private Long deletedBy;

    private LocalDateTime deletedDate;

    public String getProductCategoryByLanguage(Language language) {
        language = checkLanguage(language);

        switch (language) {
            case EN:
                return englishName;
            case UZ:
                return uzbekName;
            case RU:
            default:
                return russianName;
        }
    }
}