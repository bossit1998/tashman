package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uz.tm.tashman.models.SignInModel;
import uz.tm.tashman.models.SignUpModel;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    @Qualifier("jdbcMaster")
    private JdbcTemplate jdbcTemplate;


    public List<Map<String, Object>> index() {
        return null;
    }

    public List<Map<String, Object>> products () {
        return null;
    }

    public List<Map<String, Object>> sign_in(SignInModel signInModel) {
        return null;
    }

    public List<Map<String, Object>> sign_up(SignUpModel signUpModel) {
        return null;
    }

    public List<Map<String, Object>> place_order() {
        return null;
    }
}
