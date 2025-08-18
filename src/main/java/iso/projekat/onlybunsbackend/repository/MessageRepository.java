package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender = :s1 AND m.receiver = :r1) " +
            "   OR (m.sender = :s2 AND m.receiver = :r2) " +
            "ORDER BY m.timestamp DESC")
    List<Message> findConversation(@Param("s1") String s1,
                                   @Param("r1") String r1,
                                   @Param("s2") String s2,
                                   @Param("r2") String r2,
                                   Pageable pageable);

}
