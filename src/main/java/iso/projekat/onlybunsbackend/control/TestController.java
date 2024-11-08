package iso.projekat.onlybunsbackend.control;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

}
