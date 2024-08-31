package app.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import app.config.MYSQLConnection;
import app.dao.interfaces.PartnerDao;
import app.dto.PartnerDto;
import app.dto.UserDto;
import app.helpers.Helper;
import app.model.Partner;
import app.model.User;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PartnerDaoImplementation implements PartnerDao {
    
    @Override
    public boolean existsByPartnerId(PartnerDto partnerDto) throws Exception {
        String query = "SELECT 1 FROM PARTNER WHERE ID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, partnerDto.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exists = resultSet.next();
        resultSet.close();
        preparedStatement.close();
        return exists;
    }
        

    @Override
    public void createPartner(PartnerDto partnerDto) throws Exception {
        Partner partner = Helper.parse(partnerDto);
        String query = "INSERT INTO PARTNER (USERID, AMOUNT, TYPE, CREATIONDATE) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, partner.getUserId().getId());
        BigDecimal amount = BigDecimal.valueOf(partner.getAmount());
        preparedStatement.setBigDecimal(2, amount);
        preparedStatement.setString(3, partner.getType());
        Timestamp creationDate = Timestamp.valueOf(partner.getCreationDate());
        preparedStatement.setTimestamp(4, creationDate);
        preparedStatement.execute();
        preparedStatement.close();
    }
    
    @Override
    public void deletePartner(PartnerDto partnerDto) throws Exception {
        Partner partner = Helper.parse(partnerDto);
        String query = "DELETE FROM PARTNER WHERE ID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, partner.getId());
        preparedStatement.execute();
        preparedStatement.close();
    }
    
    @Override
    public PartnerDto findByPartnerId(PartnerDto partnerDto) throws Exception {
        String query = "SELECT ID, USERID, AMOUNT, TYPE, CREATIONDATE FROM PARTNER WHERE ID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, partnerDto.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Partner partner = new Partner();
            partner.setId(resultSet.getLong("ID"));
            partner.setAmount(resultSet.getBigDecimal("AMOUNT").doubleValue());
            partner.setType(resultSet.getString("TYPE"));
            Timestamp timestamp = resultSet.getTimestamp("CREATIONDATE");
            LocalDateTime creationDate = timestamp.toLocalDateTime();
            partner.setCreationDate(creationDate);
            User user = new User();
            user.setId(resultSet.getLong("USERID"));
            partner.setUserId(user);
            resultSet.close();
            preparedStatement.close();
            return Helper.parse(partner);
        }
        resultSet.close();
        preparedStatement.close();
        return null;
    }
    
    @Override
    public void updatePartner(PartnerDto partnerDto){
        //pass
    }
    
    @Override
    public long countPartnersVip() throws Exception {
        String query = "SELECT COUNT(ID) AS NUMBERVIP FROM PARTNER WHERE TYPE = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, "VIP");
        ResultSet resulSet = preparedStatement.executeQuery();
        if (resulSet.next()) {
            long numberVip = resulSet.getLong("NUMBERVIP") ;
            resulSet.close();
            preparedStatement.close();
            return numberVip;
        }
        resulSet.close();
        preparedStatement.close();        
        return 0;
    }
} 
