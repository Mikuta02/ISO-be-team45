package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "pracenje")
public class Pracenje {
    @EmbeddedId
    private PracenjeId id;

    @MapsId("pratilacId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pratilac_id", nullable = false)
    private Korisnik pratilac;

    @MapsId("praceniId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "praceni_id", nullable = false)
    private Korisnik praceni;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "vreme_pocetka")
    private Instant vremePocetka;

}