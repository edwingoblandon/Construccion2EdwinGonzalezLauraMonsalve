package app.service;

import java.sql.SQLException;

import app.dao.*;
import app.dao.interfaces.*;
import app.dto.GuestDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.service.interfaces.AdminService;
import app.service.interfaces.LoginService;
import app.service.interfaces.PartnerService;
import app.dto.UserDto;
import app.helpers.Helper;
import app.model.Partner;


public class Service implements AdminService, LoginService , PartnerService{
    private final PersonDao personDao;
    private final UserDao userDao;
    private final PartnerDao partnerDao;
    private final  GuestDao guestDao;
    private DetailInvoiceDao detailInvoiceDao;
    private InvoiceDao invoiceDao;
    
    public static UserDto user;
    
    public Service(){
        this.personDao = new PersonDaoImplementation();
        this.userDao = new UserDaoImplementation();
        this.partnerDao = new PartnerDaoImplementation();
        this.guestDao = new GuestDaoImplementation();
    }
    
    @Override
    public void createPartner(PartnerDto partnerDto) throws Exception{
        this.createPartnerInDb(partnerDto);
    }
    
    @Override
    public void createGuest(GuestDto guestDto) throws Exception{
        this.createGuestInDb(guestDto);
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
        userDto.setPersonId(validateDto.getPersonId());
        userDto.setId(validateDto.getId());
        user = userDto;
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
    
    
    private void createPartnerInDb(PartnerDto partnerDto) throws Exception {
        this.createUser(partnerDto.getUserId());
        UserDto userDto = userDao.findByUserName(partnerDto.getUserId());
        PersonDto personDto = personDao.findByDocument(partnerDto.getUserId().getPersonId());
        partnerDto.setUserId(userDto);
        
        if (partnerDto.getType().equalsIgnoreCase("VIP") && this.partnerDao.countPartnersVip() >= 5 ) {
            this.userDao.deleteUser(userDto);
            this.personDao.deletePerson(personDto);
            throw new Exception("Ya existen 5 socios VIP");
        }

        try {
            this.partnerDao.createPartner(partnerDto);
        } catch (SQLException e) {
            System.out.println("Ocurrio un error: " + e.getMessage());
        }
    }

    
    private void createGuestInDb(GuestDto guestDto) throws Exception{
        this.createUser(guestDto.getUserId());
        UserDto userDto = userDao.findByUserName(guestDto.getUserId());
        PersonDto personDto = personDao.findByDocument(guestDto.getUserId().getPersonId());
        guestDto.setUserId(userDto);
        try {
            this.guestDao.createGuest(guestDto);
        } catch(SQLException e){
            System.out.println("Ocurrio un error: " + e.getMessage());
            this.userDao.deleteUser(guestDto.getUserId());
            this.personDao.deletePerson(personDto);
        }
        
    }
}