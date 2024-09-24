package app.service.interfaces;

import app.dto.GuestDto;
import app.dto.PartnerDto;
import app.dto.UserDto;

public interface PartnerService {
    public void createGuest(GuestDto guestDto) throws Exception;
    public void activateGuest(GuestDto guestDto) throws Exception;
    public void inactivateGuest(GuestDto guestDto) throws Exception;
    public void requestToUnsubscribe() throws Exception;
    public PartnerDto getSessionPartner() throws Exception;
}
