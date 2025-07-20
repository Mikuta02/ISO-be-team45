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

    private String sender;   // Korisnik koji šalje poruku
    private String receiver; // Korisnik kome se šalje poruka
    private String content;  // Sadržaj poruke

    private LocalDateTime timestamp = LocalDateTime.now(); // Vreme slanja poruke
}
