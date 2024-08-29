package app.service.interfaces;

import app.dto.UserDto;

public interface AdminService {
    public void createPartner(UserDto userDto) throws Exception;
}
