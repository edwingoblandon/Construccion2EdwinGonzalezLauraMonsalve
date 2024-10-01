package app.dao.repository;

import app.model.Invoice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    @Query("SELECT i FROM Invoice i WHERE i.partnerId.id = :partnerId")
    List<Invoice> findByPartnerId(@Param("partnerId") Long partnerId);
    
    @Query("SELECT i FROM Invoice i WHERE i.userId.id = :userId")
    List<Invoice> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT i FROM Invoice i WHERE i.partnerId.id = :partnerId AND i.status = 'Pending' ORDER BY i.creationDate ASC")
    List<Invoice> findPendingInvoicesByPartnerId(@Param("partnerId") Long partnerId);
}
