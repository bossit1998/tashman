package uz.tm.tashman.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Language;
import uz.tm.tashman.enums.TimeUnits;


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

    private Double price;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private Long createdBy;
    private Boolean in_production;
    private String bar_code;
    private Integer store_temperature;
    private LocalDateTime first_launched_date;
    private String brand;

    private Integer expireDuration;
    @Enumerated(EnumType.STRING)
    private TimeUnits expireDurationUnit;


    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory category;
    @Column(columnDefinition = "TEXT")
    private String fullDescriptionEn;
    @Column(columnDefinition = "TEXT")
    private String fullDescriptionRu;
    @Column(columnDefinition = "TEXT")
    private String fullDescriptionUz;
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "product")
    private List<Assortment> assortments;

    @JsonBackReference
    @OneToOne
    private ProductPacking productPacking;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductImage> image;


    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    private Long deletedBy;

    private LocalDateTime deletedDate;


    public String getNameByLanguage(Language language) {
        return getString(language, nameEn, nameUz, nameRu);
    }

    public String getShortDescriptionByLanguage(Language language) {
        return getString(language, shortDescriptionEn, shortDescriptionUz, shortDescriptionRu);
    }

    public static String getString(Language language, String shortDescriptionEn, String shortDescriptionUz, String shortDescriptionRu) {
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
        return getString(language, fullDescriptionEn, fullDescriptionUz, fullDescriptionRu);
    }
}