package app.dao.repository;

import app.model.DetailInvoice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailInvoiceRepository extends JpaRepository<DetailInvoice, Long>{
    
    @Query("SELECT di FROM DetailInvoice di JOIN di.invoiceId inv WHERE inv.partnerId.id = :partnerId")
    List<DetailInvoice> findByPartnerId(@Param("partnerId") Long partnerId);
}
