package gr.aueb.cf.e_commerce_app.repository;

import gr.aueb.cf.e_commerce_app.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
