package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.tm.tashman.entity.User;
import uz.tm.tashman.enums.Gender;
import uz.tm.tashman.enums.Language;
import uz.tm.tashman.enums.ProductCategory;
import uz.tm.tashman.enums.VolumeUnit;
import uz.tm.tashman.models.BasicModel;
import uz.tm.tashman.models.HashMapModel;
import uz.tm.tashman.util.HTTPUtil;

import java.util.List;

import static uz.tm.tashman.enums.StatusCodes.*;

@Service
public class CommonService extends HTTPUtil {

    @Autowired
    LogService logService;

    public ResponseEntity<?> getGenderList(BasicModel basicModel) {
        try {
            if (basicModel.getLanguage()==null) {
                basicModel.setLanguage(Language.RU);
            }

            List<HashMapModel> genderList = Gender.getAllByLanguage(basicModel.getLanguage());

            return OkResponse(SUCCESS, genderList);
        } catch (Exception e) {
            logService.saveToLog(exceptionAsString(e));
            return InternalServerErrorResponse(exceptionAsString(e));
        }
    }

    public ResponseEntity<?> getProductCategoryList(BasicModel basicModel) {
        try {
            User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<HashMapModel> productCategoryList = ProductCategory.getAllByLanguage(admin.getLanguage());

            return OkResponse(SUCCESS, productCategoryList);
        } catch (Exception e) {
            logService.saveToLog(exceptionAsString(e));
            return InternalServerErrorResponse(exceptionAsString(e));
        }
    }

    public ResponseEntity<?> getVolumeUnitList(BasicModel basicModel) {
        try {
            User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<HashMapModel> volumeUnitList = VolumeUnit.getAllByLanguage(admin.getLanguage());

            return OkResponse(SUCCESS, volumeUnitList);
        } catch (Exception e) {
            logService.saveToLog(exceptionAsString(e));
            return InternalServerErrorResponse(exceptionAsString(e));
        }
    }
}