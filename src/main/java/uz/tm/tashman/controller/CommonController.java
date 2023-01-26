package uz.tm.tashman.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
