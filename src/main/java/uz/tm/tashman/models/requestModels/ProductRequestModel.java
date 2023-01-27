package uz.tm.tashman.models.requestModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import uz.tm.tashman.enums.TimeUnits;
import uz.tm.tashman.enums.VolumeUnit;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRequestModel implements Serializable {
    private Long id;
    private String encodedId;
    private String slug;
    private String metaTitleEn;
    private String metaTitleRu;
    private String metaTitleUz;
    private String metaDescriptionEn;
    private String metaDescriptionRu;
    private String metaDescriptionUz;
    private String productCategory;
    private String nameEn;
    private String nameRu;
    private String nameUz;
    private String shortDescriptionEn;
    private String shortDescriptionRu;
    private String shortDescriptionUz;
    private String fullDescriptionEn;
    private String fullDescriptionRu;
    private String fullDescriptionUz;
    private Integer piecesPerPackage;
    private Double packageNettoWeight;
    private Double packageBruttoWeight;
    private String packageDimensions;
    private Double volume;
    private VolumeUnit volumeUnit;
    private Integer expireDuration;
    private TimeUnits expireDurationUnit;
    private List<MultipartFile> images;
    private List<String> imageUrls;
    private List<String> thumbnailImageUrls;
    private Double price;
}