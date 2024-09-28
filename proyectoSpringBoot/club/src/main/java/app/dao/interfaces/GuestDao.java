package app.dao.interfaces;

import app.dto.GuestDto;
import app.dto.PartnerDto;
import java.util.List;


public interface GuestDao {
    
    public void createGuest(GuestDto guestDto) throws Exception;
    
    public boolean existsById(GuestDto guestDto) throws Exception;
    
    public GuestDto findById(GuestDto guestDto) throws Exception;
    
    public void updateGuest(GuestDto guestDto) throws Exception;

    public int countActiveGuestsByPartnerId(GuestDto guestDto) throws Exception;
    
    public List<GuestDto> findAllGuestsByPartnerId(PartnerDto partnerDto) throws Exception;
}
