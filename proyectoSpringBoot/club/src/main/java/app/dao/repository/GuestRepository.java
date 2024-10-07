package app.dao.repository;

import app.model.Partner;
import app.model.Guest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    int countByPartnerIdAndStatus(Partner partner, String status);
    
    @Query("SELECT g FROM Guest g WHERE g.partnerId.id = :partnerId")
    List<Guest> findByPartnerId(@Param("partnerId") Long partnerId);
    
    @Query("SELECT g FROM Guest g JOIN g.userId u WHERE u.id = :userId")
    Guest findByUserId(@Param("userId") Long userId);
}
