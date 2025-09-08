package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.AnalyticsDTO;
import iso.projekat.onlybunsbackend.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository repo;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private List<AnalyticsDTO.TimePoint> merge(List<AnalyticsRepository.Bucket> posts,
                                               List<AnalyticsRepository.Bucket> comments) {
        Map<String, Long> p = new HashMap<>();
        for (var b : posts) {
            String key = b.getPeriod().toInstant().atZone(ZoneOffset.UTC).toLocalDate().toString();
            p.put(key, b.getCnt());
        }
        Map<String, Long> c = new HashMap<>();
        for (var b : comments) {
            String key = b.getPeriod().toInstant().atZone(ZoneOffset.UTC).toLocalDate().toString();
            c.put(key, b.getCnt());
        }
        // zadrži sve periode koje imamo u oba seta (ili bar jednom), sortiraj opadajuće po datumu
        Set<String> all = new HashSet<>();
        all.addAll(p.keySet());
        all.addAll(c.keySet());
        List<String> periods = new ArrayList<>(all);
        periods.sort(Comparator.reverseOrder());

        List<AnalyticsDTO.TimePoint> out = new ArrayList<>();
        for (String period : periods) {
            out.add(new AnalyticsDTO.TimePoint(
                    period,
                    p.getOrDefault(period, 0L),
                    c.getOrDefault(period, 0L)
            ));
        }
        return out;
    }

    public AnalyticsDTO getAll() {
        // time-series
        var weekly   = merge(repo.postsByWeek(),   repo.commentsByWeek());
        var monthly  = merge(repo.postsByMonth(),  repo.commentsByMonth());
        var yearly   = merge(repo.postsByYear(),   repo.commentsByYear());

        // user distribution
        long total = repo.totalUsers();
        long posters = repo.usersWithPosts();
        long commentersOnly = repo.usersWithOnlyComments();
        long inactive = Math.max(0, total - posters - commentersOnly);

        var dist = new AnalyticsDTO.UserDistribution(total, posters, commentersOnly, inactive);

        // vraćamo liste u HRONOLOŠKOM redosledu (od starijeg ka novijem) radi grafa
        Collections.reverse(weekly);
        Collections.reverse(monthly);
        Collections.reverse(yearly);

        return new AnalyticsDTO(weekly, monthly, yearly, dist);
    }
}
