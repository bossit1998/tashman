package uz.tm.tashman.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.BasicModel;
import uz.tm.tashman.models.ProductModel;
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
    public ResponseEntity<?> addProduct(@ModelAttribute ProductModel productModel, HttpServletRequest header) {

        if (isBlank(productModel.getSlug())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getProductCategory())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getName())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getShortDescription())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getPiecesPerPackage())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getVolume())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getVolumeUnit())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getExpireDuration())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getPrice())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getMetaTitle())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getMetaDescription())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getFullDescription())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getPackageNettoWeight())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getPackageBruttoWeight())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }
        if (isBlank(productModel.getPackageDimensions())) {
            return BadRequestResponse(StatusCodes.DEVICE_INFO_MISSING);
        }

        return productService.addProduct(productModel, header);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @PostMapping(value = "/list")
    public ResponseEntity<?> list(@RequestBody BasicModel basicModel, HttpServletRequest header) {
        return productService.list(basicModel, header);
    }
}
