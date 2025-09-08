package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.AnalyticsDTO;
import iso.projekat.onlybunsbackend.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService service;

    @GetMapping
    public AnalyticsDTO getAnalytics() {
        return service.getAll();
    }
}
