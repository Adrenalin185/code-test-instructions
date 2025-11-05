package tpx.test.url_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tpx.test.url_service.entities.Url;

public interface URLRepository extends JpaRepository<Url,Integer> {
}
