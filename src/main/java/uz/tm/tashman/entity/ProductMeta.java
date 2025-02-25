package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Language;

import javax.persistence.*;

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


    public String getMetaTitle(Language language) {
        switch (language) {
            case EN:
                return metaTitleEn;
            case UZ:
                return metaTitleUz;
            case RU:
            default:
                return metaTitleRu;
        }
    }

    public String getMetaDescription(Language language) {
        switch (language) {
            case EN:
                return metaDescriptionEn;
            case UZ:
                return metaDescriptionUz;
            case RU:
            default:
                return metaDescriptionRu;
        }
    }
}
