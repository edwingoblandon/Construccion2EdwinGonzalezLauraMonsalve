package app.service.interfaces;

import app.dto.DetailInvoiceDto;
import app.dto.InvoiceDto;
import app.dto.UserDto;

public interface InvoiceService {
    public void createInvoice(UserDto userDto,InvoiceDto invoiceDto, DetailInvoiceDto detailInvoiceDto) throws Exception;
}
