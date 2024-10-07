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
        String query = "SELECT g.ID, g.USERID, g.PARTNERID, g.STATUS, u.PERSONID " +
                       "FROM GUEST g " +
                       "JOIN USER u ON g.USERID = u.ID " +
                       "WHERE g.ID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, guestDto.getId());

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Guest guest = new Guest();
            guest.setId(resultSet.getLong("ID"));

            User user = new User();
            user.setId(resultSet.getLong("USERID"));

            Partner partner = new Partner();
            partner.setId(resultSet.getLong("PARTNERID"));
            
            Person person = new Person();
            person.setId(resultSet.getLong("PERSONID"));
            if(person == null) throw new Exception("Error el person recuperado es null");
            user.setPersonId(person);
           
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
    }
    
    @Override
    public void updateGuest(GuestDto guestDto) throws Exception {
        String query = "UPDATE GUEST SET USERID = ?, PARTNERID = ?, STATUS = ? WHERE ID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);

        preparedStatement.setLong(1, guestDto.getUserId().getId());
        preparedStatement.setLong(2, guestDto.getPartnerId().getId());
        preparedStatement.setString(3, guestDto.getStatus());
        preparedStatement.setLong(4, guestDto.getId());

        preparedStatement.executeUpdate();

        preparedStatement.close();
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
    }   
}

