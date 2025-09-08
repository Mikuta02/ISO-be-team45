package iso.projekat.onlybunsbackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.sql.Timestamp;
import java.util.List;

import iso.projekat.onlybunsbackend.model.Post; // <-- prilagodi tačan paket/ime entiteta

public interface AnalyticsRepository extends Repository<Post, Long> {

    interface Bucket {
        Timestamp getPeriod();
        long getCnt();
    }

    // POSTS
    @Query(value = "SELECT date_trunc('week', p.created_at) AS period, COUNT(*) AS cnt " +
            "FROM posts p GROUP BY period ORDER BY period DESC LIMIT 8", nativeQuery = true)
    List<Bucket> postsByWeek();

    @Query(value = "SELECT date_trunc('month', p.created_at) AS period, COUNT(*) AS cnt " +
            "FROM posts p GROUP BY period ORDER BY period DESC LIMIT 12", nativeQuery = true)
    List<Bucket> postsByMonth();

    @Query(value = "SELECT date_trunc('year', p.created_at) AS period, COUNT(*) AS cnt " +
            "FROM posts p GROUP BY period ORDER BY period DESC LIMIT 5", nativeQuery = true)
    List<Bucket> postsByYear();

    // COMMENTS
    @Query(value = "SELECT date_trunc('week', c.created_at) AS period, COUNT(*) AS cnt " +
            "FROM comments c GROUP BY period ORDER BY period DESC LIMIT 8", nativeQuery = true)
    List<Bucket> commentsByWeek();

    @Query(value = "SELECT date_trunc('month', c.created_at) AS period, COUNT(*) AS cnt " +
            "FROM comments c GROUP BY period ORDER BY period DESC LIMIT 12", nativeQuery = true)
    List<Bucket> commentsByMonth();

    @Query(value = "SELECT date_trunc('year', c.created_at) AS period, COUNT(*) AS cnt " +
            "FROM comments c GROUP BY period ORDER BY period DESC LIMIT 5", nativeQuery = true)
    List<Bucket> commentsByYear();

    // USER DISTRIBUTION
    @Query(value = "SELECT COUNT(*) FROM users", nativeQuery = true)
    long totalUsers();

    @Query(value = "SELECT COUNT(DISTINCT p.user_id) FROM posts p", nativeQuery = true)
    long usersWithPosts();

    @Query(value = """
            SELECT COUNT(*) FROM (
              SELECT DISTINCT c.author_id
              FROM comments c
              WHERE c.author_id NOT IN (SELECT DISTINCT p.user_id FROM posts p)
            ) x
            """, nativeQuery = true)
    long usersWithOnlyComments();
}
