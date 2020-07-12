package uz.tm.tashman.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.tm.tashman.services.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tm/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/index")
    public List<Map<String, Object>> index() {
        return userService.index();
    }

    @GetMapping("/get-products")
    public List<Map<String, Object>> products() {
        return userService.products();
    }

    @GetMapping("/sign-in")
    public List<Map<String, Object>> signIn() {
        return userService.sign_in();
    }

    @GetMapping("/sign-up")
    public List<Map<String, Object>> signUp() {
        return userService.sign_up();
    }

    @GetMapping("/place_order")
    public List<Map<String, Object>> place_order() {
        return userService.place_order();
    }


}

