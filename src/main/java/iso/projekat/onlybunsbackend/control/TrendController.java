package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.NetworkTrends;
import iso.projekat.onlybunsbackend.service.TrendsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trends")
@RequiredArgsConstructor
public class TrendController {

    private final TrendsService trendsService;

    @GetMapping
    public NetworkTrends getTrends() {
        return trendsService.getTrends();
    }
}

