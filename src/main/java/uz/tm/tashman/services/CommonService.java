package uz.tm.tashman.services;

import org.springframework.stereotype.Service;
import uz.tm.tashman.util.HTTPUtil;

@Service
public class CommonService extends HTTPUtil {

    final LogService logService;

    public CommonService(LogService logService) {
        this.logService = logService;
    }
}