package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.tm.tashman.entity.ProductMeta;
import uz.tm.tashman.models.ProductMetaModel;
import uz.tm.tashman.repository.ProductMetaRepository;
import uz.tm.tashman.util.HTTPUtil;

import java.util.Optional;

@Service
public class ProductMetaService extends HTTPUtil {
    @Autowired
    ProductMetaRepository productMetaRepository;

    public void saveToMeta(ProductMetaModel productMetaModel){
        ProductMeta productMeta = new ProductMeta();
        productMeta.setMetaTitleEn(productMetaModel.getMetaTitleEn());
        productMeta.setMetaTitleRu(productMetaModel.getMetaTitleRu());
        productMeta.setMetaTitleUz(productMetaModel.getMetaTitleUz());
        productMeta.setMetaDescriptionEn(productMetaModel.getMetaDescriptionEn());
        productMeta.setMetaDescriptionRu(productMetaModel.getMetaDescriptionRu());
        productMeta.setMetaDescriptionUz(productMetaModel.getMetaDescriptionUz());
        productMetaRepository.save(productMeta);
    }

    public Optional<ProductMeta> findById(Long id) {
        return productMetaRepository.findById(id);
    }

    public void save(ProductMeta productMeta) {
        productMetaRepository.save(productMeta);
    }
}