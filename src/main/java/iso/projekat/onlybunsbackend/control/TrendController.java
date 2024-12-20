package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.NetworkTrends;
import iso.projekat.onlybunsbackend.service.TrendsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trends")
@AllArgsConstructor
public class TrendController {
    private TrendsService trendsService;

    @GetMapping
    public NetworkTrends getTrends() {
        return trendsService.getTrends();
    }
}
