package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "korisnik", uniqueConstraints = {
        @UniqueConstraint(name = "korisnik_email_key", columnNames = {"email"}),
        @UniqueConstraint(name = "korisnik_korisnicko_ime_key", columnNames = {"korisnicko_ime"})
})
public class Korisnik {
    @Id
    @ColumnDefault("nextval('korisnik_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 50)
    @NotNull
    @Column(name = "korisnicko_ime", nullable = false, length = 50)
    private String korisnickoIme;

    @Size(max = 255)
    @NotNull
    @Column(name = "lozinka", nullable = false)
    private String lozinka;

    @Size(max = 50)
    @NotNull
    @Column(name = "ime", nullable = false, length = 50)
    private String ime;

    @Column(name = "adresa", length = Integer.MAX_VALUE)
    private String adresa;

    @ColumnDefault("0")
    @Column(name = "broj_pratilaca")
    private Integer brojPratilaca;

    @ColumnDefault("false")
    @Column(name = "aktiviran")
    private Boolean aktiviran;

    @Size(max = 20)
    @ColumnDefault("'korisnik'")
    @Column(name = "uloga", length = 20)
    private String uloga;

}