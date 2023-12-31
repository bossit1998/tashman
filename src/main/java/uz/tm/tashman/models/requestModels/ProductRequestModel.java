package uz.tm.tashman.models.requestModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import uz.tm.tashman.enums.TimeUnits;

import uz.tm.tashman.models.ProductMetaModel;
import uz.tm.tashman.models.ProductPackingModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRequestModel implements Serializable {
    private Long id;
    private String encodedId;
    private String slug;
    private ProductMetaModel metas;
    private String nameEn;
    private String nameRu;
    private String nameUz;
    private String productCategory;
    private ProductPackingModel productPackingModel;
    private String shortDescriptionEn;
    private String shortDescriptionRu;
    private String shortDescriptionUz;
    private String fullDescriptionEn;
    private String fullDescriptionRu;
    private String fullDescriptionUz;
    private Boolean inProduction;
    private String barCode;
    private List<String> assortments;
    private String storeTemperature;
    private LocalDateTime firstLaunchedDate;
    private String brand;
    private Integer expireDuration;
    private TimeUnits expireDurationUnit;
    private List<MultipartFile> images;
    private List<String> imageUrls;
    private List<String> thumbnailImageUrls;
    private Double price;
}