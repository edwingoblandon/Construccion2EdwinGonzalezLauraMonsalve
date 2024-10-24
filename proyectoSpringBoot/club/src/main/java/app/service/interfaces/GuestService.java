package app.service.interfaces;

import app.dto.GuestDto;


public interface GuestService {
    public void convertGuestToPartner(GuestDto guestDto) throws Exception;
}
