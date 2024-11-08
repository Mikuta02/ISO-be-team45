package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "authorities", indexes = {
        @Index(name = "idx_authorities_username", columnList = "username")
})
public class Authority {
    @Id
    @ColumnDefault("nextval('authorities_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "username", nullable = false, referencedColumnName = "username")
    private iso.projekat.onlybunsbackend.model.User username;

    @Size(max = 50)
    @NotNull
    @Column(name = "authority", nullable = false, length = 50)
    private String authority;

}