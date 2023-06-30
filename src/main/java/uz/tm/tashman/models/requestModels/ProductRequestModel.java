package uz.tm.tashman.models.requestModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import uz.tm.tashman.enums.TimeUnits;

import uz.tm.tashman.models.ProductMetaModel;

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
    private String shortDescriptionEn;
    private String shortDescriptionRu;
    private String shortDescriptionUz;
    private String fullDescriptionEn;
    private String fullDescriptionRu;
    private String fullDescriptionUz;
    private Boolean in_production;
    private String bar_code;
    private List<String> assortments;
    private Integer storeTemperature;
    private LocalDateTime first_launched_date;
    private String brand;
    private Integer expireDuration;
    private TimeUnits expireDurationUnit;
    private List<MultipartFile> images;
    private List<String> imageUrls;
    private List<String> thumbnailImageUrls;
    private Double price;
}