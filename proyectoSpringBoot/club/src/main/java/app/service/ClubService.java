package app.service;

import java.sql.SQLException;
import app.dao.interfaces.*;
import app.dto.DetailInvoiceDto;
import app.dto.GuestDto;
import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.ProductDto;
import app.service.interfaces.AdminService;
import app.service.interfaces.LoginService;
import app.service.interfaces.PartnerService;
import app.dto.UserDto;
import app.service.interfaces.GuestService;
import app.service.interfaces.InvoiceService;
import app.service.interfaces.ProductService;
import java.time.LocalDateTime;
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
public class ClubService implements AdminService, LoginService , PartnerService, GuestService, ProductService, InvoiceService{
    @Autowired
    private PersonDao personDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PartnerDao partnerDao;
    @Autowired
    private GuestDao guestDao;
    @Autowired
    private DetailInvoiceDao detailInvoiceDao;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private ProductDao productDao;
    
    public static UserDto user;
 
    
    @Override
    public void createPartner(PartnerDto partnerDto) throws Exception{
        this.createPartnerInDb(partnerDto);
    }
    
    @Override
    public void createInvoice(InvoiceDto invoiceDto, DetailInvoiceDto detailInvoiceDto) throws Exception {
        PartnerDto partnerDto = null;
        try {
            partnerDto = getSessionPartner();
        } catch (Exception e) {}
        
        invoiceDto.setPartnerId(partnerDto == null ? getSessionGuest().getPartnerId() : partnerDto);
        
        invoiceDto.setUserId(user);
        this.invoiceDao.createInvoice(invoiceDto); 
        
        try {
            this.detailInvoiceDao.createDetailInvoice(detailInvoiceDto);
        } catch(SQLException e){
            System.out.println("Ocurrio un error: " + e.getMessage());
            this.invoiceDao.deleteInvoice(invoiceDto);
        } 
    }

    @Override
    public void createGuest(GuestDto guestDto) throws Exception{
        this.createGuestInDb(guestDto);
    }
    
    @Override
    public void activateGuest(GuestDto guestDto) throws Exception{
        this.activateGuestInDb(guestDto);
    }
    
    @Override
    public void convertGuestToPartner() throws Exception{
        this.convertGuestToPartnerInDb();
    }
    
    @Override
    public void inactivateGuest(GuestDto guestDto) throws Exception{
        this.inactivateGuestInDb(guestDto);
    }
    
    @Override
    public List<GuestDto> getGuestsForPartnerSession(String status) throws Exception{
        return this.getFilteredGuests(status);
    }

    @Override
    public void unsubscribeRequest() throws Exception{
        this.unsubscribe();
    }
    
    @Override
    public void vipPromotionRequest() throws Exception{
        this.promotionVip();
    }
    
    @Override
    public void increaseFunds(double amount) throws Exception{
        this.increaseFundsInDb(amount);
    }
    
    @Override
    public List<ProductDto> getAllProducts() throws Exception{
        return productDao.findAllProducts();
    }
    
    @Override
    public List<InvoiceDto> getAllInvoices() throws Exception{
        return invoiceDao.findAllByPartnerId(getSessionPartner());
    }
    
    @Override
    public List<DetailInvoiceDto> getAllDetailInvoice() throws Exception{
        return detailInvoiceDao.findAllByPartnerId(getSessionPartner());
    }
    
    @Override
    public void login(UserDto userDto) throws Exception {
        UserDto validateDto = userDao.findByUserName(userDto);
        
        if (validateDto == null) throw new Exception("No existe este usuario registrado");
        
        if (!userDto.getPassword().equals(validateDto.getPassword())) throw new Exception("Usuario o contraseña incorrecto");
        
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
    
    private PartnerDto getSessionPartner() throws Exception {
        
        if (user == null) throw new Exception("No hay usuario en sesion.");
        
        PartnerDto partnerDto = partnerDao.findByUserId(user);
        
        if (partnerDto == null) throw new Exception("No se encontro el socio asociado al usuario en sesion");
            
        return partnerDto;
    }
    
    private GuestDto getSessionGuest() throws Exception {
        if (user == null) throw new Exception("No hay usuario en sesion.");
        
        GuestDto guestDto = guestDao.findByUserId(user);
        
        if (guestDto == null) throw new Exception("No se encontro el invitado asociado al usuario en sesion");
        
        return guestDto;
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
    
    private void activateGuestInDb(GuestDto guestDto) throws Exception{
        this.isValidGuestByPartner(guestDto, getSessionPartner());
        guestDto = guestDao.findById(guestDto);
        if ("regular".equalsIgnoreCase(guestDto.getPartnerId().getType())){
            int activeGuestCount = guestDao.countActiveGuestsByPartnerId(guestDto);
            if (activeGuestCount >= 3) throw new Exception("ERROR! Ya haz alcanzado el maximo de invitados activos");
        }
        guestDto.setStatus("Active");
        guestDao.updateGuest(guestDto);
    }
    
    private void inactivateGuestInDb(GuestDto guestDto) throws Exception{
        this.isValidGuestByPartner(guestDto, getSessionPartner());
        guestDto = guestDao.findById(guestDto);
        guestDto.setStatus("Inactive");
        guestDao.updateGuest(guestDto);
    }
    
    private void convertGuestToPartnerInDb() throws Exception{
        GuestDto guestDto = this.getSessionGuest();
        guestDto = guestDao.findById(guestDto);
        guestDto.setStatus("CONVERTED_TO_PARTNER");
        guestDao.updateGuest(guestDto);
   
        UserDto userDto = userDao.findById(guestDto.getUserId());
        userDto.setRole("partner");
        userDao.updateUser(userDto);
        
        PartnerDto partnerDto = new PartnerDto();
        partnerDto.setUserId(user);
        partnerDto.setAmount(50000);
        partnerDto.setType("regular");
        partnerDto.setCreationDate(LocalDateTime.now());

        List<InvoiceDto> invoices = invoiceDao.findAllByUserId(guestDto.getUserId());
        if(!invoices.isEmpty()){
            for(InvoiceDto invoice: invoices){
                if(invoice.getStatus().equalsIgnoreCase("Pending")) throw new Exception("Tu socio debe pagar todas tus facturas antes de promoverte a socio\nFactura id: " + invoice.getId() + " estado: " + invoice.getStatus());
            }
        }
        
        partnerDao.createPartner(partnerDto);
    }
    
    private void unsubscribe() throws Exception{
        PartnerDto partnerDto = this.getSessionPartner();
        PersonDto personDto = personDao.findByDocument(partnerDto.getUserId().getPersonId());
        
        List<InvoiceDto> invoices = invoiceDao.findAllByPartnerId(partnerDto);
        if(!invoices.isEmpty()){
            for(InvoiceDto invoice: invoices){
                if(invoice.getStatus().equalsIgnoreCase("Pending")) throw new Exception("Debes pagar todas tus facturas antes de darte de baja\nFactura id: " + invoice.getId() + " estado: " + invoice.getStatus());
            }
        }
        this.personDao.deletePerson(personDto);
    }
    
    private void promotionVip() throws Exception{
        PartnerDto partnerDto = this.getSessionPartner();
        partnerDto.setType("in progress");
        this.partnerDao.updatePartner(partnerDto);
    }
    
    private List<GuestDto> getAllGuestsBySessionPartner() throws Exception {
        PartnerDto partnerDto = getSessionPartner();

        List<GuestDto> allGuests = guestDao.findAllGuestsByPartnerId(partnerDto);

        if (allGuests.isEmpty()) throw new Exception("No hay invitados asociados al socio en sesion");

        return allGuests;
    }
    
    private List<GuestDto> getFilteredGuests(String filter) throws Exception {
        List<GuestDto> allGuests = getAllGuestsBySessionPartner();

        List<GuestDto> activeGuests = allGuests.stream()
            .filter(guest -> guest.getStatus().equalsIgnoreCase("Active"))
            .collect(Collectors.toList());
        
        List<GuestDto> inactiveGuests = allGuests.stream()
            .filter(guest -> guest.getStatus().equalsIgnoreCase("Inactive"))
            .collect(Collectors.toList());
        
        if(filter.equalsIgnoreCase("ACTIVE")){
            if (activeGuests.isEmpty()) throw new Exception("No hay invitados activos asociados al socio en sesion");
            return activeGuests;
        }
        else{
            if (inactiveGuests.isEmpty()) throw new Exception("No hay invitados inactivos asociados al socio en sesion");
            return inactiveGuests;
        }
    }
    
    private boolean isValidGuestByPartner(GuestDto guestDto, PartnerDto partnerDto) throws Exception {
        GuestDto validateDto = guestDao.findById(guestDto);
        
        if(validateDto.getPartnerId().getId() != partnerDto.getId()){
            throw new Exception("ERROR! Este ID de invitado no esta registrado");
        }
        
        return true;
    }
    
    private void increaseFundsInDb(double amount) throws Exception{
        PartnerDto partnerDto = getSessionPartner();
        double currentAmount = partnerDto.getAmount();
// implement the logic of paying bills with the recharge -----------------------------------------------------------------------------------------------------------------------------
        if( (currentAmount + amount <= 1000000) && 
            (partnerDto.getType().equalsIgnoreCase("Regular") || partnerDto.getType().equalsIgnoreCase("in progress")) &&
                (currentAmount + amount > 0) ) {
            
            partnerDto.setAmount(currentAmount + amount);
            partnerDao.updatePartner(partnerDto);
            System.out.println("Nuevo saldo disponible: " + partnerDto.getAmount());
        }
        else if( (currentAmount + amount <= 5000000) && (partnerDto.getType().equalsIgnoreCase("VIP")) &&
                (currentAmount + amount > 0) ) {
            
            partnerDto.setAmount(currentAmount + amount);
            partnerDao.updatePartner(partnerDto);
            System.out.println("Nuevo saldo disponible: " + partnerDto.getAmount());
        }
        else throw new Exception("ERROR!: El monto supera el limite de fondos acumulados");
    }
}