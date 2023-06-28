package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Language;

import javax.persistence.*;

import static uz.tm.tashman.entity.Product.getString;
import static uz.tm.tashman.util.Util.checkLanguage;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long productId;
    @Column(columnDefinition = "TEXT")
    private String metaTitleEn;
    @Column(columnDefinition = "TEXT")
    private String metaDescriptionEn;
    @Column(columnDefinition = "TEXT")
    private String metaTitleRu;
    @Column(columnDefinition = "TEXT")
    private String metaDescriptionRu;
    @Column(columnDefinition = "TEXT")
    private String metaTitleUz;
    @Column(columnDefinition = "TEXT")
    private String metaDescriptionUz;


    public String getMetaTitleByLanguage(Language language) {
        return getString(language, metaTitleEn, metaTitleUz, metaTitleRu);
    }

    public String getMetaDescriptionByLanguage(Language language) {
        return getString(language, metaDescriptionEn, metaDescriptionUz, metaDescriptionRu);
    }

}
