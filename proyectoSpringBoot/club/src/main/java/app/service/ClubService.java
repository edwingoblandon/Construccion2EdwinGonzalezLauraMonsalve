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
import java.util.List;
import java.util.stream.Collectors;
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
        this.isValidGuestByPartner(guestDto, getSessionPartner());
        guestDto = guestDao.findById(guestDto);
        if ("regular".equalsIgnoreCase(guestDto.getPartnerId().getType())){
            int activeGuestCount = guestDao.countActiveGuestsByPartnerId(guestDto);
            if (activeGuestCount >= 3) throw new Exception("ERROR! Ya haz alcanzado el maximo de invitados activos");
        }
        guestDto.setStatus("Active");
        guestDao.updateGuest(guestDto);
    }
    
    @Override
    public void inactivateGuest(GuestDto guestDto) throws Exception{
        this.isValidGuestByPartner(guestDto, getSessionPartner());
        guestDto = guestDao.findById(guestDto);
        guestDto.setStatus("Inactive");
        guestDao.updateGuest(guestDto);
    }
    
    @Override
    public void showGuestsForPartnerSession(String status) throws Exception{
        this.showGuestsForPartner(status);
    }
    
    @Override
    public void login(UserDto userDto) throws Exception {
        UserDto validateDto = userDao.findByUserName(userDto);
        
        if (validateDto == null) {
            throw new Exception("No existe este usuario registrado");
        }
        
        if (!userDto.getPassword().equals(validateDto.getPassword())) {
            throw new Exception("Usuario o contraseña incorrecto");
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
    
    private PartnerDto getSessionPartner() throws Exception {
        if (user == null) {
            throw new Exception("No hay usuario en sesion.");
        }
        PartnerDto partnerDto = partnerDao.findByUserId(user);
        if (partnerDto == null) {
            throw new Exception("No se encontro el socio asociado al usuario en sesion");
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
        if (this.partnerDao.countPartnersVip() >= 5 ) {
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
        //Here we will put the logic to validate that there are no invoices
        this.personDao.deletePerson(personDto);
        System.out.println("Persona eliminada\n EL SOCIO FUE ELIMINADO"); 
    }
    
    private List<GuestDto> getAllGuestsBySessionPartner() throws Exception {
        PartnerDto partnerDto = getSessionPartner();

        List<GuestDto> allGuests = guestDao.findAllGuestsByPartnerId(partnerDto);

        if (allGuests.isEmpty()) throw new Exception("No hay invitados asociados al socio en sesion.");

        return allGuests;
    }
    
    private void showGuestsForPartner(String filter) throws Exception {
        List<GuestDto> allGuests = getAllGuestsBySessionPartner();

        List<GuestDto> activeGuests = allGuests.stream()
            .filter(guest -> guest.getStatus().equalsIgnoreCase("Active"))
            .collect(Collectors.toList());
        
        List<GuestDto> inactiveGuests = allGuests.stream()
            .filter(guest -> guest.getStatus().equalsIgnoreCase("Inactive"))
            .collect(Collectors.toList());
        
        if(filter.equalsIgnoreCase("Active")){
            if (activeGuests.isEmpty()) throw new Exception("No hay invitados activos asociados al socio en sesion");
            System.out.println("\nInvitados activos del socio en sesión:");
            for (GuestDto guestDto : activeGuests) {
                System.out.println("ID: " + guestDto.getId());
                System.out.println("Nombre: " + guestDto.getUserId().getPersonId().getName());
                System.out.println("-------------------------------");
            }
        }
        else{
            if (inactiveGuests.isEmpty()) throw new Exception("No hay invitados inactivos asociados al socio en sesion");
            System.out.println("\nInvitados inactivos del socio en sesión:");
            for (GuestDto guestDto : inactiveGuests) {
                System.out.println("ID: " + guestDto.getId());
                System.out.println("Nombre: " + guestDto.getUserId().getPersonId().getName());
                System.out.println("-------------------------------");
            }
        }


    }
    
    private boolean isValidGuestByPartner(GuestDto guestDto, PartnerDto partnerDto) throws Exception {
        GuestDto validateDto = guestDao.findById(guestDto);
        
        if(validateDto.getPartnerId().getId() != partnerDto.getId()){
            throw new Exception("ERROR! Este ID de invitado no esta registrado");
        }
        
        return true;
    }
}