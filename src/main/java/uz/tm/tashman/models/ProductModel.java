package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import uz.tm.tashman.enums.ProductCategory;
import uz.tm.tashman.enums.VolumeUnit;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductModel {
    private Long id;
    private String encodedId;
    private String slug;
    private String metaTitle;
    private String metaDescription;
    private ProductCategory productCategory;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private Integer piecesPerPackage;
    private Double packageNettoWeight;
    private Double packageBruttoWeight;
    private String packageDimensions;
    private Double volume;
    private VolumeUnit volumeUnit;
    private String expireDuration;
    private List<MultipartFile> images;
    private List<String> imageUrls;
    private List<String> thumbnailImageUrls;
    private Double price;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private String createdBy;
    private Boolean isDeleted;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDateTime deletedDate;
    private String deletedBy;
}