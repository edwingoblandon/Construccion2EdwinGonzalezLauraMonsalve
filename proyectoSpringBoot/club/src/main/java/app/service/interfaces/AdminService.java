package app.service.interfaces;

import app.dto.DetailInvoiceDto;
import app.dto.PartnerDto;
import java.util.List;

public interface AdminService {
    public void createPartner(PartnerDto partnerDto) throws Exception;
    public List<DetailInvoiceDto> getAllDetailInvoices() throws Exception;
}
