package app.dao.interfaces;

import app.dto.DetailInvoiceDto;

public interface DetailInvoiceDao {
    public boolean existsById(DetailInvoiceDto detailInvoiceDto) throws Exception;
    public void createDetailInvoice(DetailInvoiceDto detailInvoiceDto) throws Exception;
    public DetailInvoiceDto findById(DetailInvoiceDto detailInvoiceDto) throws Exception;
    public void updateDetailInvoice(DetailInvoiceDto detailInvoiceDto) throws Exception;
}
