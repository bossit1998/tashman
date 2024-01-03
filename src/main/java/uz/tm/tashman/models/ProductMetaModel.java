package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductMetaModel implements Serializable {
    private Long id;
    private Long productId;
    private String metaTitleEn;
    private String metaTitleRu;
    private String metaTitleUz;
    private String metaDescriptionEn;
    private String metaDescriptionRu;
    private String metaDescriptionUz;
}