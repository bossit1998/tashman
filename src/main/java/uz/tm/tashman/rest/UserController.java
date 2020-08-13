package uz.tm.tashman.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tm.tashman.models.ResponseData;
import uz.tm.tashman.models.SignInModel;
import uz.tm.tashman.models.SignUpModel;
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

    @PostMapping("/sign-in")
    public List<Map<String, Object>> signIn(@RequestBody SignInModel signInModel) {
        return userService.sign_in(signInModel);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpModel signUpModel) {
        return userService.sign_up(signUpModel);
    }

    @GetMapping("/place_order")
    public List<Map<String, Object>> place_order() {
        return userService.place_order();
    }


}

