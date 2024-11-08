package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "analitika")
public class Analitika {
    @Id
    @ColumnDefault("nextval('analitika_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "datum", nullable = false)
    private LocalDate datum;

    @ColumnDefault("0")
    @Column(name = "broj_objava")
    private Integer brojObjava;

    @ColumnDefault("0")
    @Column(name = "broj_komentara")
    private Integer brojKomentara;

    @Column(name = "procent_aktivnih")
    private Double procentAktivnih;

}