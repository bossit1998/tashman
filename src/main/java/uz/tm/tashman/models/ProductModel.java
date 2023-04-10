package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.entity.Assortment;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductModel implements Serializable {
    private Long id;
    private String encodedId;
    private String slug;
    private String metaTitle;
    private String metaDescription;
    private String category;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private Integer piecesPerPackage;
    private Double packageNettoWeight;
    private Double packageBruttoWeight;
    private String packageDimensions;
    private Double volume;
    private String volumeUnit;
    private Integer expireDuration;
    private String expireDurationUnit;
    private List<AssortmentResponseModel> assortments;
    private List<ProductImageModel> images;
    private Double price;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private String createdBy;
    private Boolean isDeleted;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDateTime deletedDate;
    private String deletedBy;
    private Boolean in_production;
    private String bar_code;
    private String flavor;
    private Integer store_temperature;
    private LocalDateTime first_launched_date;
    private String brand;
}