package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "group_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "username"}))
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="group_id", nullable = false)
    private Long groupId;

    @Column(nullable = false)
    private String username; // član grupe
}
