package app.dao.interfaces;

import app.dto.DetailInvoiceDto;

public interface DetailInvoiceDao {
    public void createDetailInvoice(DetailInvoiceDto detailInvoiceDto) throws Exception;
}
