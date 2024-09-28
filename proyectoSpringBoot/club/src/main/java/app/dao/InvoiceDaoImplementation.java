package app.dao;


import app.dao.interfaces.InvoiceDao;
import app.dao.repository.InvoiceRepository;
import app.dto.InvoiceDto;
import app.helpers.Helper;
import app.model.Invoice;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@Service
public class InvoiceDaoImplementation implements InvoiceDao{
    
    @Autowired
    InvoiceRepository invoiceRepository;
    
    @Override
    public boolean existsById(InvoiceDto invoiceDto) throws Exception{
        return invoiceRepository.existsById(invoiceDto.getId());
    }
    
    @Override
    public InvoiceDto findById(InvoiceDto invoiceDto) throws Exception{
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceDto.getId());
        
        if(!optionalInvoice.isPresent()) throw new Exception("La factura no se encontro");
        
        Invoice invoice = optionalInvoice.get();
        return Helper.parse(invoice);
    }
    
    @Override
    public void createInvoice(InvoiceDto invoiceDto) throws Exception{
        Invoice invoice = Helper.parse(invoiceDto);
        invoiceRepository.save(invoice);
        invoiceDto.setId(invoice.getId());
    }
    
    @Override
    public void updateInvoice(InvoiceDto invoiceDto) throws Exception{
        Invoice invoice = Helper.parse(invoiceDto);
        invoiceRepository.save(invoice);
    }
}
