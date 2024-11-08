package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "komentar")
public class Komentar {
    @Id
    @ColumnDefault("nextval('komentar_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "objava_id")
    private iso.projekat.onlybunsbackend.model.Objava objava;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "korisnik_id")
    private iso.projekat.onlybunsbackend.model.Korisnik korisnik;

    @NotNull
    @Column(name = "tekst", nullable = false, length = Integer.MAX_VALUE)
    private String tekst;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "vreme_kreiranja")
    private Instant vremeKreiranja;

}