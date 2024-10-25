package dev.folomkin.mockito.web_demo;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    @RequestMapping("/")
    public @ResponseBody String greeting() {
        return "Hello, World";
    }
}


