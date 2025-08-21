package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    boolean existsByGroupIdAndUsername(Long groupId, String username);
    void deleteByGroupIdAndUsername(Long groupId, String username);
    List<GroupMember> findByGroupId(Long groupId);

    // liste grupa po korisniku
    List<GroupMember> findByUsername(String username);
}
