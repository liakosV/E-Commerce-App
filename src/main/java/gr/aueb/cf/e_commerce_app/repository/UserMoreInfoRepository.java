package gr.aueb.cf.e_commerce_app.repository;

import gr.aueb.cf.e_commerce_app.model.UserMoreInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserMoreInfoRepository extends JpaRepository<UserMoreInfo, Long>, JpaSpecificationExecutor<UserMoreInfo> {

    Optional<UserMoreInfo> findByPhoneNumber(String phoneNumber);
}
