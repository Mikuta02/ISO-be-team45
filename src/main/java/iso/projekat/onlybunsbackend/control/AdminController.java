package iso.projekat.onlybunsbackend.control;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/home-links")
    public Map<String, Object> links() {
        return Map.of(
                "links", List.of(
                        Map.of("label", "Sve objave", "path", "/admin/posts"),
                        Map.of("label", "Trendovi mreže", "path", "/trends"),
                        Map.of("label", "Analitika aplikacije", "path", "/admin/analytics"),
                        Map.of("label", "Svi profili", "path", "/admin/users")
                )
        );
    }
}
