package app.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import app.config.MYSQLConnection;
import app.dao.interfaces.GuestDao;
import app.dto.GuestDto;
import app.dto.UserDto;
import app.helpers.Helper;
import app.model.Guest;
import app.model.Partner;
import app.model.Person;
import app.model.User;

public class GuestDaoImplementation implements GuestDao {
    
    @Override
    public boolean existsByGuestId(GuestDto guestDto) throws Exception {
        String query = "SELECT 1 FROM GUEST WHERE ID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, guestDto.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exists = resultSet.next();
        resultSet.close();
        preparedStatement.close();
        return exists;
    }
    
    @Override
    public void createGuest(GuestDto guestDto) throws Exception {
        Guest guest = Helper.parse(guestDto);
        String query = "INSERT INTO GUEST (USERID, PARTNERID, STATUS) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, guest.getUserId().getId());
        preparedStatement.setLong(2, guest.getPartnerId().getId());
        preparedStatement.setString(3, guest.getStatus());
        preparedStatement.execute();
        preparedStatement.close();
    }
    
    @Override
    public void deleteGuest(GuestDto guestDto) throws Exception {
        Guest guest = Helper.parse(guestDto);
        String query = "DELETE FROM GUEST WHERE ID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, guest.getId());
        preparedStatement.execute();
        preparedStatement.close();
    }
    
    @Override
    public GuestDto findByGuestId(GuestDto guestDto) throws Exception {
        String query = "SELECT ID, USERID, PARTNERID, STATUS FROM GUEST WHERE ID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, guestDto.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Guest guest = new Guest();
            guest.setId(resultSet.getLong("ID"));
            guest.setStatus(resultSet.getString("STATUS"));
            
            User user = new User();
            user.setId(resultSet.getLong("USERID"));
            
            Partner partner = new Partner();
            partner.setId(resultSet.getLong("PARTNERID"));
            
            guest.setUserId(user);
            guest.setPartnerId(partner);
            resultSet.close();
            preparedStatement.close();
            return Helper.parse(guest);
        }
        resultSet.close();
        preparedStatement.close();
        return null;
    }
    
    @Override
    public void updateGuest(GuestDto guestDto){
        //pass
    }
    
    
    @Override
    public void updateGuestStatus(GuestDto guestDto) throws Exception {
        String query = "UPDATE GUEST SET STATUS = ? WHERE ID = ? ";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setString( 1, guestDto.getStatus() );
        preparedStatement.setLong( 2, guestDto.getId());

        preparedStatement.execute();
        preparedStatement.close();
    }
    
    @Override
    public GuestDto findByUserId(UserDto userDto) throws Exception {
        String query = "SELECT ID, USERID, PARTNERID, STATUS FROM GUEST WHERE USERID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, userDto.getId());

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Guest guest = new Guest();
            guest.setId(resultSet.getLong("ID"));
            User user = new User();
            user.setId(resultSet.getLong("USERID"));
            guest.setUserId(user);
            Partner partner = new Partner();
            partner.setId(resultSet.getLong("PARTNERID"));
            guest.setPartnerId(partner);
            guest.setStatus(resultSet.getString("STATUS"));

            resultSet.close();
            preparedStatement.close();
            return Helper.parse(guest);
        }

        resultSet.close();
        preparedStatement.close();
        return null;
    }   
}

