package app.service;

import java.sql.SQLException;
import app.dao.interfaces.*;
import app.dto.GuestDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.service.interfaces.AdminService;
import app.service.interfaces.LoginService;
import app.service.interfaces.PartnerService;
import app.dto.UserDto;
import app.service.interfaces.GuestService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@Service
public class ClubService implements AdminService, LoginService , PartnerService, GuestService{
    @Autowired
    private PersonDao personDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PartnerDao partnerDao;
    @Autowired
    private GuestDao guestDao;
    private DetailInvoiceDao detailInvoiceDao;
    private InvoiceDao invoiceDao;
    
    public static UserDto user;
 
    
    @Override
    public void createPartner(PartnerDto partnerDto) throws Exception{
        this.createPartnerInDb(partnerDto);
    }
    
    @Override
    public void createGuest(GuestDto guestDto) throws Exception{
        this.createGuestInDb(guestDto);
    }
    
    @Override
    public void activateGuest(GuestDto guestDto) throws Exception{
        guestDto = guestDao.findById(guestDto);
        guestDto.setStatus("Active");
        guestDao.updateGuest(guestDto);
    }
    
    @Override
    public void inactivateGuest(GuestDto guestDto) throws Exception{
        guestDto = guestDao.findById(guestDto);
        guestDto.setStatus("Inactive");
        guestDao.updateGuest(guestDto);
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
    
    
    @Override
    public void requestToUnsubscribe() throws Exception{
        this.unsubscribe();
    }
    
    @Override
    public PartnerDto getSessionPartner() throws Exception {
        if (user == null) {
            throw new Exception("No hay usuario en sesión.");
        }
        PartnerDto partnerDto = partnerDao.findByUserId(user);
        if (partnerDto == null) {
            throw new Exception("No se encontro el socio asociado al usuario en sesion.");
        }
        return partnerDto;
    }
    
    private void createPerson(PersonDto personDto) throws Exception {
        if (this.personDao.existsByDocument(personDto)) {
            throw new Exception("ya existe una persona con ese documento");
        }
        
        this.personDao.createPerson(personDto);
        
    }
    
    private void createUser(UserDto userDto) throws Exception {
        this.createPerson(userDto.getPersonId());

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
        
        //¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡ Apply in promotions of REGULAR to VIP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (partnerDto.getType().equalsIgnoreCase("VIP") && this.partnerDao.countPartnersVip() >= 5 ) {
            this.personDao.deletePerson(partnerDto.getUserId().getPersonId());
            throw new Exception("Ya existen 5 socios VIP");
        }//pass

        try {
            this.partnerDao.createPartner(partnerDto);
        } catch (SQLException e) {
            System.out.println("Ocurrio un error: " + e.getMessage());
            this.personDao.deletePerson(partnerDto.getUserId().getPersonId());
        }
    }

    
    private void createGuestInDb(GuestDto guestDto) throws Exception{
        this.createUser(guestDto.getUserId());
        guestDto.setPartnerId(getSessionPartner());
        
        if ("regular".equalsIgnoreCase(guestDto.getPartnerId().getType())) {
            int activeGuestCount = guestDao.countActiveGuestsByPartnerId(guestDto);
            if (activeGuestCount >= 3) throw new Exception("Haz alcanzado el maximo de invitados activos");
        }
        
        try {
            this.guestDao.createGuest(guestDto);
        } catch(SQLException e){
            System.out.println("Ocurrio un error: " + e.getMessage());
            this.personDao.deletePerson(guestDto.getUserId().getPersonId());
        }    
    }
    
    private void unsubscribe() throws Exception{
        PartnerDto partnerDto = this.getSessionPartner();
        PersonDto personDto = personDao.findByDocument(partnerDto.getUserId().getPersonId());
        
        UserDto userDto = userDao.findByUserName(partnerDto.getUserId());//Check
        if (userDto == null) throw new Exception("No se encontró el usuario con el nombre de usuario proporcionado --");
        
        userDto.setPersonId(personDto);
        partnerDto.setUserId(userDto);
       
        
        if(partnerDto == null) throw new Exception("El partner no puede ser nulo");
        if(personDto == null) throw new Exception("El person no puede ser nulo");
        if(userDto == null) throw new Exception("El user no puede ser nulo");
        try {
            this.personDao.deletePerson(personDto);
            System.out.println("Persona eliminada\n EL SOCIO FUE ELIMINADO");
        } catch (SQLException e) {
            this.personDao.deletePerson(userDto.getPersonId());
        }
    }
    
    /*private void activateGuestInDb(GuestDto guestDto) throws Exception{
        if (guestDto == null) {
            throw new Exception("El invitado no puede ser nulo.");
        }

        GuestDto existingGuest = guestDao.findByGuestId(guestDto);
        if (existingGuest == null) {
            throw new Exception("El invitado no existe.");
        }
        
        guestDto.setStatus("active");
   
        this.updateGuestStatus(guestDto);
        System.out.println("El invitado ha sido activado correctamente.");
    }*/
    
    /*private void inactivateGuestInDb(GuestDto guestDto) throws Exception{
        if (guestDto == null) {
            throw new Exception("El invitado no puede ser nulo.");
        }

        GuestDto existingGuest = guestDao.findByGuestId(guestDto);
        if (existingGuest == null) {
            throw new Exception("El invitado no existe.");
        }
        
        guestDto.setStatus("inactive");
   
        this.updateGuestStatus(guestDto);
        System.out.println("El invitado ha sido desactivado correctamente.");
    }*/

    
}