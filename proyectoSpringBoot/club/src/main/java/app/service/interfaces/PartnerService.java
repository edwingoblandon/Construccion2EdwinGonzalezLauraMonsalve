package app.service.interfaces;

import app.dto.GuestDto;

public interface PartnerService {
    public void createGuest(GuestDto guestDto) throws Exception;
    public void activateGuest(GuestDto guestDto) throws Exception;
    public void inactivateGuest(GuestDto guestDto) throws Exception;
    public void requestToUnsubscribe() throws Exception;
    public void showGuestsForPartnerSession(String status) throws Exception;
}
