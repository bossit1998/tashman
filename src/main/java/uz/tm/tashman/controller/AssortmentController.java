package uz.tm.tashman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.services.AssortmentService;
import uz.tm.tashman.util.HTTPUtil;

import java.util.List;

@RestController
@RequestMapping("/assortment")
@CrossOrigin(origins = "*")
public class AssortmentController extends HTTPUtil {

    final
    AssortmentService assortmentService;

    public AssortmentController(AssortmentService assortmentService) {
        this.assortmentService = assortmentService;
    }

    public ResponseEntity<?> editAssortmentList(@RequestBody List<String> assortments, @RequestParam Long productId){
        if(assortments.isEmpty()){
            return BadRequestResponse(StatusCodes.ASSORTMENT_NOT_FOUND);
        }
        if(productId == null){
            return BadRequestResponse(StatusCodes.PRODUCT_INFO_MISSING);
        }

        return assortmentService.editAssortmentList(assortments,productId);
    }
}
