package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;       // username pošiljaoca
    private String receiver;     // username primaoca (privatno) - za grupu = null
    private String content;

    @Column(name = "is_group", nullable = false)
    private Boolean group = false;    // privatno=false, grupno=true

    // za grupne:
    @Column(name = "group_id")
    private Long groupId;             // null za privatne

    private LocalDateTime timestamp = LocalDateTime.now();
}
