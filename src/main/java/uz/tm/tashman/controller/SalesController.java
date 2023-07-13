package uz.tm.tashman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.SalesRequestModel;
import uz.tm.tashman.services.SalesService;
import uz.tm.tashman.util.HTTPUtil;

import static uz.tm.tashman.util.Util.isBlank;

@RestController
@RequestMapping("/sales")
@CrossOrigin(origins = "*")
public class SalesController extends HTTPUtil {

    @Autowired
    SalesService salesService;

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @PostMapping("/sell")
    public ResponseEntity<?> sell(@RequestBody SalesRequestModel sellingRequestModel) {
        if (sellingRequestModel == null) {
            return BadRequestResponse(StatusCodes.SELLING_REQUEST_NOT_FOUND);
        }
        //TODO status codes need to be fixed
        if (isBlank(sellingRequestModel.getProductId())
                || isBlank(sellingRequestModel.getAssortmentId())
                || isBlank(sellingRequestModel.getClientId())
                || isBlank(sellingRequestModel.getAmount())) {
            return BadRequestResponse(StatusCodes.SELLING_REQUEST_INFO_NOT_FOUND);
        }
        return salesService.sell(sellingRequestModel);
    }

}
