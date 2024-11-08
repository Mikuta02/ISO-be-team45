package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PracenjeId implements java.io.Serializable {
    private static final long serialVersionUID = 5752917470953508313L;
    @NotNull
    @Column(name = "pratilac_id", nullable = false)
    private Integer pratilacId;

    @NotNull
    @Column(name = "praceni_id", nullable = false)
    private Integer praceniId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PracenjeId entity = (PracenjeId) o;
        return Objects.equals(this.praceniId, entity.praceniId) &&
                Objects.equals(this.pratilacId, entity.pratilacId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(praceniId, pratilacId);
    }

}