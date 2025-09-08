package iso.projekat.onlybunsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnalyticsDTO {

    @Data
    @AllArgsConstructor
    public static class TimePoint {
        private String period; // npr. 2025-09-01 (početak nedelje/meseca/godine)
        private long posts;
        private long comments;
    }

    @Data
    @AllArgsConstructor
    public static class UserDistribution {
        private long totalUsers;
        private long posters;           // ≥1 objava ikada
        private long commentersOnly;    // 0 objava, ≥1 komentar
        private long inactive;          // 0 objava i 0 komentara

        public double getPostersPercent() {
            return totalUsers == 0 ? 0 : (posters * 100.0 / totalUsers);
        }
        public double getCommentersOnlyPercent() {
            return totalUsers == 0 ? 0 : (commentersOnly * 100.0 / totalUsers);
        }
        public double getInactivePercent() {
            return totalUsers == 0 ? 0 : (inactive * 100.0 / totalUsers);
        }
    }

    private List<TimePoint> weekly;   // poslednjih 8 nedelja
    private List<TimePoint> monthly;  // poslednjih 12 meseci
    private List<TimePoint> yearly;   // poslednjih 5 godina
    private UserDistribution distribution;
}
