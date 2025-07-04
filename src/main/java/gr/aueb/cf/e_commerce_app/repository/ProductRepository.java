package gr.aueb.cf.e_commerce_app.repository;

import gr.aueb.cf.e_commerce_app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByName(String name);
    Optional<Product> findByUuid(String uuid);
}
