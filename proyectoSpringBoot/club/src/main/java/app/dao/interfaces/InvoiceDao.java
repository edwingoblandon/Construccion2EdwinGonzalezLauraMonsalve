package app.dao.interfaces;

import app.dto.InvoiceDto;

public interface InvoiceDao {
    public void createInvoice(InvoiceDto invoiceDto) throws Exception;
    public boolean existsById(InvoiceDto invoiceDto) throws Exception;
    public InvoiceDto findById(InvoiceDto invoiceDto) throws Exception;
    public void updateInvoice(InvoiceDto invoiceDto) throws Exception;
}
