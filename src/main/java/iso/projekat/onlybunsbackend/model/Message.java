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

    private String sender;       // korisničko ime pošiljaoca
    private String receiver;     // korisničko ime primaoca (za privatni chat)
    private String content;      // tekst poruke
    @Column(name = "is_group")
    @Getter
    private Boolean group = false; // privatna=false, grupna=true

    private LocalDateTime timestamp = LocalDateTime.now();

}
