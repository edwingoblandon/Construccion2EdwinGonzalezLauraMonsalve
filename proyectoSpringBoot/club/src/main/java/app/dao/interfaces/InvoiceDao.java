package app.dao.interfaces;

import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import app.dto.UserDto;
import java.util.List;

public interface InvoiceDao {
    public void createInvoice(InvoiceDto invoiceDto) throws Exception;
    public boolean existsById(InvoiceDto invoiceDto) throws Exception;
    public InvoiceDto findById(InvoiceDto invoiceDto) throws Exception;
    public void updateInvoice(InvoiceDto invoiceDto) throws Exception;
    public void deleteInvoice(InvoiceDto invoiceDto) throws Exception;
    public List<InvoiceDto> findAllByPartnerId(PartnerDto partnerDto) throws Exception;
    public List<InvoiceDto> findAllByUserId(UserDto userDto) throws Exception;
    public List<InvoiceDto> findPendingInvoicesByPartnerId(PartnerDto partnerDto) throws Exception;
}
