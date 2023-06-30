package uz.tm.tashman.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.BasicModel;
import uz.tm.tashman.models.requestModels.ProductRequestModel;
import uz.tm.tashman.services.ProductService;
import uz.tm.tashman.util.HTTPUtil;

import javax.servlet.http.HttpServletRequest;

import static uz.tm.tashman.util.Util.isBlank;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*")
public class ProductController extends HTTPUtil {

    final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<?> addProduct(@ModelAttribute ProductRequestModel productModel, HttpServletRequest header) {

        if (isBlank(productModel.getSlug())
                || isBlank(productModel.getProductCategory())
                || isBlank(productModel.getNameEn())
                || isBlank(productModel.getNameRu())
                || isBlank(productModel.getNameUz())
                || isBlank(productModel.getShortDescriptionEn())
                || isBlank(productModel.getShortDescriptionRu())
                || isBlank(productModel.getShortDescriptionUz())

                || isBlank(productModel.getProductPackingModel().getPiecesPerPackage())
                || isBlank(productModel.getProductPackingModel().getVolume())
                || isBlank(productModel.getProductPackingModel().getVolumeUnit())
                || isBlank(productModel.getExpireDurationUnit())
                || isBlank(productModel.getPrice())
//                || isBlank(productModel.getMetas())
//                || isBlank(productModel.getMetas().getMetaTitleEn())
//                || isBlank(productModel.getMetas().getMetaTitleRu())
//                || isBlank(productModel.getMetas().getMetaTitleUz())
//                || isBlank(productModel.getMetas().getMetaDescriptionEn())
//                || isBlank(productModel.getMetas().getMetaDescriptionRu())
//                || isBlank(productModel.getMetas().getMetaDescriptionUz())
                || isBlank(productModel.getFullDescriptionEn())
                || isBlank(productModel.getFullDescriptionRu())
                || isBlank(productModel.getFullDescriptionUz())
                || isBlank(productModel.getProductPackingModel().getPackageNettoWeight())
                || isBlank(productModel.getProductPackingModel().getPackageBruttoWeight())
                || isBlank(productModel.getProductPackingModel().getPackageDimensions())
        ){
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        return productService.addProduct(productModel, header);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/edit")
    public ResponseEntity<?> editProduct(@RequestBody ProductRequestModel productRequestModel, HttpServletRequest header){
        return productService.editProduct(productRequestModel,header);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @PostMapping(value = "/list")
    public ResponseEntity<?> list(@RequestBody BasicModel basicModel, HttpServletRequest header) {

        return productService.list(basicModel, header);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/getProductCategoryList")
    public ResponseEntity<?> getProductCategoryList(@RequestBody BasicModel basicModel) {

        return productService.getProductCategoryList(basicModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/getVolumeUnitList")
    public ResponseEntity<?> getVolumeUnitList(@RequestBody BasicModel basicModel) {

        return productService.getVolumeUnitList(basicModel);
    }
}
