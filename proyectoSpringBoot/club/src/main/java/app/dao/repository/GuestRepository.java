package app.dao.repository;

import app.model.Partner;
import app.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    int countByPartnerIdAndStatus(Partner partner, String status);
}
