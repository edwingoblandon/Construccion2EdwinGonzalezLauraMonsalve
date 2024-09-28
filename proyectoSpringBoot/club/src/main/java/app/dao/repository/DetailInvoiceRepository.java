package app.dao.repository;

import app.model.DetailInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailInvoiceRepository extends JpaRepository<DetailInvoice, Long>{
    
}
