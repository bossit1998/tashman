package uz.tm.tashman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.BasicModel;
import uz.tm.tashman.models.ProductModel;
import uz.tm.tashman.services.CommonService;
import uz.tm.tashman.services.ProductService;
import uz.tm.tashman.util.HTTPUtil;

import javax.servlet.http.HttpServletRequest;

import static uz.tm.tashman.util.Util.isBlank;

@RestController
@RequestMapping("/common")
@CrossOrigin(origins = "*")
public class CommonController extends HTTPUtil {

    @Autowired
    CommonService commonService;

    @PostMapping(value = "/getGenderList")
    public ResponseEntity<?> getGenderList(@RequestBody BasicModel basicModel) {

        return commonService.getGenderList(basicModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/getProductCategoryList")
    public ResponseEntity<?> getProductCategoryList(@RequestBody BasicModel basicModel) {

        return commonService.getProductCategoryList(basicModel);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/getVolumeUnitList")
    public ResponseEntity<?> getVolumeUnitList(@RequestBody BasicModel basicModel) {

        return commonService.getVolumeUnitList(basicModel);
    }
}
