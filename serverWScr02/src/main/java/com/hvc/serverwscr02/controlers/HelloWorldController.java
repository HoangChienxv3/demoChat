package com.hvc.serverwscr02.controlers;

import com.hvc.serverwscr02.security.controller.BaserController;
import com.hvc.serverwscr02.security.model.Users;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController implements BaserController {

    @RequestMapping({"/hello"})
    public Users firstPage() {
        return getUser();
    }

}