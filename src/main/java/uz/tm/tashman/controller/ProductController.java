package uz.tm.tashman.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.enums.Language;
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

        if (isBlank(productModel.getSlug())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getProductCategory())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getNameEn())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getNameRu())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getNameUz())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getShortDescriptionEn())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getShortDescriptionRu())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getShortDescriptionUz())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getPiecesPerPackage())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getVolume())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getVolumeUnit())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getExpireDurationUnit())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getPrice())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getMetaTitleEn())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getMetaTitleRu())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getMetaTitleUz())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getMetaDescriptionEn())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getMetaDescriptionRu())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getMetaDescriptionUz())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getFullDescriptionEn())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getFullDescriptionRu())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getFullDescriptionUz())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getPackageNettoWeight())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getPackageBruttoWeight())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }
        if (isBlank(productModel.getPackageDimensions())) {
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }

        return productService.addProduct(productModel, header);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @PostMapping(value = "/list")
    public ResponseEntity<?> list(@RequestBody BasicModel basicModel, HttpServletRequest header) {
        if (isBlank(basicModel.getLanguage())) {
            basicModel.setLanguage(Language.RU);
        }
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
