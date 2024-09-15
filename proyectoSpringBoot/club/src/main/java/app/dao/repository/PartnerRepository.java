package app.dao.repository;

import app.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PartnerRepository extends JpaRepository<Partner,Long>{
    long countByType(String type);
    
    @Query("SELECT p FROM Partner p JOIN p.userId u WHERE u.id = :userId")
    Partner findByUserId(@Param("userId") Long userId);
    
}
