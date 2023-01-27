package uz.tm.tashman.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Language;
import uz.tm.tashman.enums.TimeUnits;
import uz.tm.tashman.enums.VolumeUnit;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static uz.tm.tashman.util.Util.checkLanguage;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "slug")})
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String slug;
    private String nameEn;
    private String nameRu;
    private String nameUz;
    private String shortDescriptionEn;
    private String shortDescriptionRu;
    private String shortDescriptionUz;
    private Integer piecesPerPackage;
    private Double packageNettoWeight;
    private Double packageBruttoWeight;
    private String packageDimensions;
    private Double volume;
    private Double price;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private Long createdBy;

    private Integer expireDuration;
    @Enumerated(EnumType.STRING)
    private TimeUnits expireDurationUnit;
    @Enumerated(EnumType.STRING)
    private VolumeUnit volumeUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory productCategory;

    @Column(columnDefinition = "TEXT")
    private String metaTitleEn;
    @Column(columnDefinition = "TEXT")
    private String metaDescriptionEn;
    @Column(columnDefinition = "TEXT")
    private String fullDescriptionEn;
    @Column(columnDefinition = "TEXT")
    private String metaTitleRu;
    @Column(columnDefinition = "TEXT")
    private String metaDescriptionRu;
    @Column(columnDefinition = "TEXT")
    private String fullDescriptionRu;
    @Column(columnDefinition = "TEXT")
    private String metaTitleUz;
    @Column(columnDefinition = "TEXT")
    private String metaDescriptionUz;
    @Column(columnDefinition = "TEXT")
    private String fullDescriptionUz;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductImage> image;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    private Long deletedBy;

    private LocalDateTime deletedDate;


    public String getMetaTitleByLanguage(Language language) {
        language = checkLanguage(language);

        switch (language) {
            case EN:
                return metaTitleEn;
            case UZ:
                return metaTitleUz;
            case RU:
            default:
                return metaTitleRu;
        }
    }

    public String getMetaDescriptionByLanguage(Language language) {
        language = checkLanguage(language);

        switch (language) {
            case EN:
                return metaDescriptionEn;
            case UZ:
                return metaDescriptionUz;
            case RU:
            default:
                return metaDescriptionRu;
        }
    }

    public String getNameByLanguage(Language language) {
        language = checkLanguage(language);

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

    public String getShortDescriptionByLanguage(Language language) {
        language = checkLanguage(language);

        switch (language) {
            case EN:
                return shortDescriptionEn;
            case UZ:
                return shortDescriptionUz;
            case RU:
            default:
                return shortDescriptionRu;
        }
    }

    public String getFullDescriptionByLanguage(Language language) {
        language = checkLanguage(language);

        switch (language) {
            case EN:
                return fullDescriptionEn;
            case UZ:
                return fullDescriptionUz;
            case RU:
            default:
                return fullDescriptionRu;
        }
    }
}