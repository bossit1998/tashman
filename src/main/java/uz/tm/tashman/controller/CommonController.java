package uz.tm.tashman.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.models.BasicModel;
import uz.tm.tashman.services.CommonService;
import uz.tm.tashman.util.HTTPUtil;

@RestController
@RequestMapping("/common")
@CrossOrigin(origins = "*")
public class CommonController extends HTTPUtil {

    final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

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
