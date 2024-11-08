package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
