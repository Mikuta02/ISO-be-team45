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
@Table(name = "objava")
public class Objava {
    @Id
    @ColumnDefault("nextval('objava_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;

    @NotNull
    @Column(name = "opis", nullable = false, length = Integer.MAX_VALUE)
    private String opis;

    @Column(name = "slika")
    private byte[] slika;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "vreme_kreiranja")
    private Instant vremeKreiranja;
    @ColumnDefault("0")
    @Column(name = "broj_lajkova")
    private Integer brojLajkova;

/*
 TODO [Reverse Engineering] create field to map the 'lokacija' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "lokacija", columnDefinition = "point")
    private Object lokacija;
*/
}