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
    @Column(columnDefinition = "TEXT")
    private String fullDescriptionEn;
    @Column(columnDefinition = "TEXT")
    private String fullDescriptionRu;
    @Column(columnDefinition = "TEXT")
    private String fullDescriptionUz;

    private Double currentPrice;

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

    private String brandName;
    private String storeTemperature;
    private String barCode;

    private Integer expireDuration;
    @Enumerated(EnumType.STRING)
    private TimeUnits expireDurationUnit;
    private LocalDateTime firstLaunchedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory category;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private List<Assortment> assortments;

    @JsonBackReference
    @OneToOne
    private ProductPacking productPacking;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductImage> images;

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

    public String getShortDescription(Language language) {
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

    public String getFullDescription(Language language) {
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