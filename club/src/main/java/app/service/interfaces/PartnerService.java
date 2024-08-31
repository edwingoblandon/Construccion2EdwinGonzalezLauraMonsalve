package app.service.interfaces;

import app.dto.GuestDto;
import app.dto.PartnerDto;
import app.dto.UserDto;

public interface PartnerService {
    public void createGuest(GuestDto guestDto) throws Exception;
    //public void activateGuest(UserDto userDto) throws Exception;
    //public void inactivateGuest(UserDto userDto) throws Exception;
    //public void requestToUnsubscribe(UserDto userDto) throws Exception;
}
