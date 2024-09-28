package app.dao;

import app.dao.interfaces.UserDao;
import app.dao.repository.UserRepository;
import app.dto.UserDto;
import app.helpers.Helper;
import app.model.User;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@Service
public class UserDaoImplementation implements UserDao {

    @Autowired
    UserRepository userRepository;
    
    @Override
    public UserDto findById(UserDto userDto) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userDto.getId());
        
        if(!optionalUser.isPresent()) throw new Exception("El usuario no se encontro");
        
        User user = optionalUser.get();
        
        return Helper.parse(user);
    }
    
    @Override
    public UserDto findByUserName(UserDto userDto) throws Exception {
        User user = userRepository.findByUserName(userDto.getUserName());
        if (user == null) throw new Exception("No existe este usuario registrado");
        return Helper.parse(user);
    }

    @Override
    public boolean existsByUserName(UserDto userDto) throws Exception {
        return userRepository.existsByUserName(userDto.getUserName());
        }

    @Override
    public void createUser(UserDto userDto) throws Exception {
        User user = Helper.parse(userDto);
        userRepository.save(user);
        userDto.setId(user.getId());
    }

    @Override
    public void updateUser(UserDto userDto) throws Exception {
        User user = Helper.parse(userDto);
        userRepository.save(user);
    }
}
