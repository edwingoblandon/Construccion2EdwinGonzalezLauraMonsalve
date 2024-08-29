package app.service.interfaces;

import app.dto.UserDto;

public interface PartnerService {
    public void createGuest(UserDto userDto) throws Exception;
    //public void activateGuest(UserDto userDto) throws Exception;
    //public void inactivateGuest(UserDto userDto) throws Exception;
    //public void requestToUnsubscribe(UserDto userDto) throws Exception;
}
