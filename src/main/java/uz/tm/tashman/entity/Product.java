package uz.tm.tashman.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.ProductCategory;
import uz.tm.tashman.enums.VolumeUnit;

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
    private String slug;
    private String name;
    private String shortDescription;
    private Integer piecesPerPackage;
    private Double packageNettoWeight;
    private Double packageBruttoWeight;
    private String packageDimensions;
    private Double volume;
    private String expireDuration;
    private Double price;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private Long createdBy;

    @Enumerated(EnumType.STRING)
    private VolumeUnit volumeUnit;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column(columnDefinition = "TEXT")
    private String metaTitle;
    @Column(columnDefinition = "TEXT")
    private String metaDescription;
    @Column(columnDefinition = "TEXT")
    private String fullDescription;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductImage> image;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    private Long deletedBy;

    private LocalDateTime deletedDate;
}