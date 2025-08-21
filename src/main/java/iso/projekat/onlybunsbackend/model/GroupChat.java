package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "group_chats")
public class GroupChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;            // naziv grupe
    private String adminUsername;   // korisnik koji je admin (kreator)
    private LocalDateTime createdAt = LocalDateTime.now();
}
