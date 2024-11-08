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
@Table(name = "notifikacija")
public class Notifikacija {
    @Id
    @ColumnDefault("nextval('notifikacija_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;

    @NotNull
    @Column(name = "sadrzaj", nullable = false, length = Integer.MAX_VALUE)
    private String sadrzaj;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "vreme_slanja")
    private Instant vremeSlanja;

}