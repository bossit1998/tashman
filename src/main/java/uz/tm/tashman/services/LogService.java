package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.tm.tashman.entity.Logs;
import uz.tm.tashman.repository.LogsRepository;
import uz.tm.tashman.util.HTTPUtil;

@Service
public class LogService extends HTTPUtil {

    @Autowired
    private LogsRepository logsRepository;

    public void saveToLog(Exception exception) {
        logsRepository.save(new Logs(exceptionAsString(exception)));
    }

    public void saveToLog(String message) {
        logsRepository.save(new Logs(message));
    }
}