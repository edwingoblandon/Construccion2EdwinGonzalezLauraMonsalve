package app.dao;


import app.dao.interfaces.DetailInvoiceDao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import app.dao.repository.DetailInvoiceRepository;
import app.dto.DetailInvoiceDto;
import app.helpers.Helper;
import app.model.DetailInvoice;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@Service
public class DetailInvoiceDaoImplementation implements DetailInvoiceDao {
    
    @Autowired
    DetailInvoiceRepository detailInvoiceRepository;
    
    @Override
    public boolean existsById(DetailInvoiceDto detailInvoiceDto) throws Exception{
        return detailInvoiceRepository.existsById(detailInvoiceDto.getId());
    }
    
    @Override
    public void createDetailInvoice(DetailInvoiceDto detailInvoiceDto) throws Exception{
        DetailInvoice detailInvoice = Helper.parse(detailInvoiceDto);
        detailInvoiceRepository.save(detailInvoice);
        detailInvoiceDto.setId(detailInvoice.getId());
    }
    
    @Override
    public DetailInvoiceDto findById(DetailInvoiceDto detailInvoiceDto) throws Exception{
        Optional<DetailInvoice> optionalDetailInvoice = detailInvoiceRepository.findById(detailInvoiceDto.getId());
        
        if(!optionalDetailInvoice.isPresent()) throw new Exception("No se encontro los detalles de factura");
        
        DetailInvoice detailInvoice = optionalDetailInvoice.get();
        return Helper.parse(detailInvoice);
    }
    
    @Override
    public void updateDetailInvoice(DetailInvoiceDto detailInvoiceDto) throws Exception{
        DetailInvoice detailInvoice = Helper.parse(detailInvoiceDto);
        detailInvoiceRepository.save(detailInvoice);
    }
}
