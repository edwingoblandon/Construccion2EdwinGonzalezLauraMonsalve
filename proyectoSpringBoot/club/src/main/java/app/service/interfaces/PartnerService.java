package app.service.interfaces;

import app.dto.DetailInvoiceDto;
import app.dto.GuestDto;
import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import java.util.List;

public interface PartnerService {
    public void createGuest(GuestDto guestDto) throws Exception;
    public void activateGuest(GuestDto guestDto) throws Exception;
    public void inactivateGuest(GuestDto guestDto) throws Exception;
    public void unsubscribeRequest(PartnerDto partnerDto) throws Exception;
    public void vipPromotionRequest(PartnerDto partnerDto) throws Exception;
    public List<GuestDto> getGuestsForPartnerSession(String status) throws Exception;
    public void increaseFunds(double amount, PartnerDto partnerDto) throws Exception;
    public List<DetailInvoiceDto> getAllDetailInvoiceByPartner(PartnerDto partnerDto) throws Exception;
    public List<InvoiceDto> getAllPendingInvoices() throws Exception;
    public boolean payInvoice(PartnerDto partnerDto, InvoiceDto invoiceDto) throws Exception;
}
