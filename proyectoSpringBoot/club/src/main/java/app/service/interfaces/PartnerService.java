package app.service.interfaces;

import app.dto.DetailInvoiceDto;
import app.dto.GuestDto;
import app.dto.InvoiceDto;
import java.util.List;

public interface PartnerService {
    public void createGuest(GuestDto guestDto) throws Exception;
    public void activateGuest(GuestDto guestDto) throws Exception;
    public void inactivateGuest(GuestDto guestDto) throws Exception;
    public void unsubscribeRequest() throws Exception;
    public void vipPromotionRequest() throws Exception;
    public List<GuestDto> getGuestsForPartnerSession(String status) throws Exception;
    public void increaseFunds(double amount) throws Exception;
    public List<InvoiceDto> getAllInvoicesByPartner() throws Exception;
    public List<DetailInvoiceDto> getAllDetailInvoiceByPartner() throws Exception;
    public List<InvoiceDto> getAllPendingInvoices() throws Exception;
}
