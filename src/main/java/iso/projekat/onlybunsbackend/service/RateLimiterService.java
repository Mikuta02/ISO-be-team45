package iso.projekat.onlybunsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class RateLimiterService {
    private final ConcurrentHashMap<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> requestTimestamps = new ConcurrentHashMap<>();

    public boolean isAllowed(String ipAddress) {
        LocalDateTime now = LocalDateTime.now();
        requestTimestamps.putIfAbsent(ipAddress, now);
        requestCounts.putIfAbsent(ipAddress, 0);

        if (requestTimestamps.get(ipAddress).plusMinutes(1).isBefore(now)) {
            requestTimestamps.put(ipAddress, now);
            requestCounts.put(ipAddress, 0);
        }

        if (requestCounts.get(ipAddress) < 5) {
            requestCounts.put(ipAddress, requestCounts.get(ipAddress) + 1);
            return true;
        }

        return false;
    }
}
