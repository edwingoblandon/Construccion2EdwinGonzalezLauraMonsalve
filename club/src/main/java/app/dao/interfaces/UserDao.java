package app.dao.interfaces;

import app.dto.UserDto;

public interface UserDao {
    
    public void createUser(UserDto userDto) throws Exception;
    
    public boolean existsByUserName(UserDto userDto) throws Exception;
    
    public UserDto findByUserName(UserDto userDto) throws Exception;
    
    public void deleteUser(UserDto userDto) throws Exception;
}
