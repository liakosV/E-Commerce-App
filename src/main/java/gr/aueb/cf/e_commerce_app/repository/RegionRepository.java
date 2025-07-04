package gr.aueb.cf.e_commerce_app.repository;

import gr.aueb.cf.e_commerce_app.model.static_data.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long>, JpaSpecificationExecutor<Region> {

    Optional<Region> findByName(String name);
}
