package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.RabbitLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RabbitLocationRepository extends JpaRepository<RabbitLocation, String> {
}
