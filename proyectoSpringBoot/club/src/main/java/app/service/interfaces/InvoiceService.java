package app.service.interfaces;

import app.dto.DetailInvoiceDto;
import app.dto.InvoiceDto;

public interface InvoiceService {
    public void createInvoice(InvoiceDto invoiceDto, DetailInvoiceDto detailInvoiceDto) throws Exception;
}
