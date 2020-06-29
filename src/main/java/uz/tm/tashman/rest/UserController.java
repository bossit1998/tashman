package uz.tm.tashman.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tm")
public class UserController {

    @GetMapping("/index")
    public void index() {

    }
}
