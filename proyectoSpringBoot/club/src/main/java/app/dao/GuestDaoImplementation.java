package app.dao;

import app.dao.interfaces.GuestDao;
import app.dao.repository.GuestRepository;
import app.dto.GuestDto;
import app.helpers.Helper;
import app.model.Guest;


import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Setter
@Getter
@NoArgsConstructor
@Service
public class GuestDaoImplementation implements GuestDao {
    
    @Autowired
    GuestRepository guestRepository;
    
    @Override
    public boolean existsById(GuestDto guestDto) throws Exception {
        return guestRepository.existsById(guestDto.getId());
    }
    
    @Override
    public void createGuest(GuestDto guestDto) throws Exception {
        Guest guest = Helper.parse(guestDto);
        guestRepository.save(guest);
    }
    
    @Override
    public GuestDto findById(GuestDto guestDto) throws Exception {
        Optional<Guest> optionalGuest = guestRepository.findById(guestDto.getId());
        
        if(!optionalGuest.isPresent()) throw new Exception("El invitado no se encontro");
        
        Guest guest = optionalGuest.get();
        
        return Helper.parse(guest);
    }
    
    @Override
    public void updateGuest(GuestDto guestDto) throws Exception {
        //pass
    }
    
    
    @Override
    public void updateGuestStatus(GuestDto guestDto) throws Exception {
        //pass
    }
    
    /*@Override
    public GuestDto findByUserId(UserDto userDto) throws Exception {
        String query = "SELECT p.ID, p.USERID, p.TYPE, p.CREATIONDATE, p.AMOUNT, u.PERSONID " +
                   "FROM PARTNER p " +
                   "JOIN USER u ON p.USERID = u.ID " +
                   "WHERE p.USERID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, userDto.getId());

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Guest guest = new Guest();
            guest.setId(resultSet.getLong("ID"));
            
            User user = new User();
            user.setId(resultSet.getLong("USERID"));
            
            Person person = new Person();
            person.setId(resultSet.getLong("PERSONID"));
            user.setPersonId(person);
            
            Partner partner = new Partner();
            partner.setId(resultSet.getLong("PARTNERID"));
            
            guest.setUserId(user);
            guest.setPartnerId(partner);
            guest.setStatus(resultSet.getString("STATUS"));

            resultSet.close();
            preparedStatement.close();
            return Helper.parse(guest);
        }

        resultSet.close();
        preparedStatement.close();
        return null;
    }   */
}

