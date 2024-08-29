package app.service;

import java.sql.Date;
import java.sql.SQLException;

import app.dao.*;
import app.dao.interfaces.*;
import app.dto.PersonDto;
import app.service.interfaces.AdminService;
import app.service.interfaces.LoginService;
import app.service.interfaces.PartnerService;
import app.service.interfaces.GuestService;
import app.dto.UserDto;


public class Service implements AdminService, LoginService , PartnerService{
    private PersonDao personDao;
    private UserDao userDao;
    private PartnerDao partnerDao;
    private GuestDao guestDao;
    private DetailInvoiceDao detailInvoiceDao;
    private InvoiceDao invoiceDao;
    
    public static UserDto user;
    
    public Service(){
        this.personDao = new PersonDaoImplementation();
        this.userDao = new UserDaoImplementation();
    }
    
    @Override
    public void createPartner(UserDto userDto) throws Exception{
        this.createUser(userDto);
    }
    
    @Override
    public void createGuest(UserDto userDto) throws Exception{
        this.createUser(userDto);
    }
    
    @Override
    public void login(UserDto userDto) throws Exception {
        UserDto validateDto = userDao.findByUserName(userDto);
        if (validateDto == null) {
            throw new Exception("no existe este usuario registrado");
        }
        if (!userDto.getPassword().equals(validateDto.getPassword())) {
            throw new Exception("usuario o contraseña incorrecto");
        }
        userDto.setRole(validateDto.getRole());
        user = validateDto;
    }
        
    @Override
    public void logout() {
        user = null;
        System.out.println("se ha cerrado sesion");
    }
    
    private void createPerson(PersonDto personDto) throws Exception {
        if (this.personDao.existsByDocument(personDto)) {
            throw new Exception("ya existe una persona con ese documento");
        }
        this.personDao.createPerson(personDto);
    }
    
    private void createUser(UserDto userDto) throws Exception {
        this.createPerson(userDto.getPersonId());
        PersonDto personDto = personDao.findByDocument(userDto.getPersonId());
        userDto.setPersonId(personDto);
        if (this.userDao.existsByUserName(userDto)) {
            this.personDao.deletePerson(userDto.getPersonId());
            throw new Exception("ya existe un usuario con ese user name");
        }
        try {
            this.userDao.createUser(userDto);
        } catch (SQLException e) {
            this.personDao.deletePerson(userDto.getPersonId());
        }
    }
}
