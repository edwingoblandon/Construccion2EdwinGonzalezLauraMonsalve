package app.dao.interfaces;

import app.dto.GuestDto;

public interface GuestDao {
    
    public void createGuest(GuestDto guestDto) throws Exception;
    
    public boolean existsByGuestId(GuestDto guestDto) throws Exception;
    
    public GuestDto findByGuestId(GuestDto guestDto) throws Exception;
    
    public void deleteGuest(GuestDto guestDto) throws Exception;
    
    public void updateGuest(GuestDto guestDto) throws Exception;
    
}
