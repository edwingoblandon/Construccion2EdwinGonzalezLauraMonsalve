
package app.controller;

import app.controller.validator.AdminValidator;
import app.controller.validator.PartnerValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.dto.DetailInvoiceDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.service.ClubService;
import app.service.interfaces.AdminService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Getter
@Setter
@NoArgsConstructor
@Controller
public class AdminController implements ControllerInterface {
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private PartnerValidator partnerValidator;
    @Autowired
    private AdminValidator adminValidator;
    
    @Autowired
    private AdminService service;
    private static final String MENU = "Ingrese la el numero de la opcion \n1. Crear socio\n2. Ver facturas club\n3. Promover a VIP\n4. Cerrar sesion";

    @Override
    public void session() throws Exception {
        boolean session = true;
        while (session) {
            session = menu();
        }
    }
    
    private boolean options(String option) throws Exception{
        switch(option){
            case "1":{
                this.createPartner();
                return true;
            }
            case "2":
                this.showAllInvoices();
                return true;
            case "3":
                this.promoteToVip();
                return true;
            case "4": {
                System.out.println("Se ha cerrado sesion con exito.");
                return false;
            }
            default: {
                System.out.println("Opcion incorrecta, intentelo de nuevo");
                return true;
            }
        }
    }
    
    private boolean menu(){
        try{
            System.out.println("bienvenido(a) " + ClubService.user.getUserName());
            System.out.println(MENU);
            String option = Utils.getReader().nextLine();
            return options(option);
        } catch(Exception e){
            System.out.println(e.getMessage());
            return true;
        }
    }
    
    private void createPartner() throws Exception{
        System.out.println("Ingrese el nombre del socio");
        String name = Utils.getReader().nextLine(); 
        personValidator.validName(name); //Void method
        System.out.println("Ingrese el numero de documento del socio");
        long document = personValidator.validDocument(Utils.getReader().nextLine()); //Long method
        System.out.println("Ingrese el numero de celular del socio");
        long celphone = personValidator.validCellPhone(Utils.getReader().nextLine());
        
        System.out.println("Ingrese el nombre de usuario del socio");
        String userName = Utils.getReader().nextLine();
        userValidator.validUserName(userName);
        System.out.println("Ingrese una contraseña para el socio");
        String password = Utils.getReader().nextLine();
        userValidator.validPassword(password);
        
        String type = ("regular");
        double amount = 50000;
        
        
        PersonDto personDto = new PersonDto();
        personDto.setName(name);
        personDto.setDocument(document);
        personDto.setCellPhone(celphone);
        
        UserDto userDto = new UserDto();
        userDto.setPersonId(personDto);
        userDto.setUserName(userName);
        userDto.setPassword(password);
        userDto.setRole("partner");
        
        PartnerDto partnerDto = new PartnerDto();
        partnerDto.setUserId(userDto);
        partnerDto.setAmount(amount);
        partnerDto.setType(type);
        partnerDto.setCreationDate(LocalDateTime.now());

        this.service.createPartner(partnerDto);
        System.out.println("Se ha creado el usuario exitosamente");
    }
    
    private void showAllInvoices() throws Exception {
        List <DetailInvoiceDto> invoices = this.service.getAllDetailInvoices();
        System.out.println("\n***Facturas***");
        for (DetailInvoiceDto invoice : invoices) {
            System.out.println("ID: " + invoice.getInvoiceId().getId());
            
            String status = invoice.getInvoiceId().getStatus().equalsIgnoreCase("Pending") ? "Pendiente" : "Pagada";
            System.out.println("Estado: " + status);
            
            System.out.println("Persona que realizo el consumo: " + invoice.getInvoiceId().getUserId().getPersonId().getName());
            String type = invoice.getInvoiceId().getUserId().getRole().equalsIgnoreCase("Partner") ? "Socio" : "Invitado";
            
            System.out.println("Tipo de persona que realizo el consumo: " + type);
            System.out.println("Fecha de la factura: " + invoice.getInvoiceId().getDateOfCreation());
            System.out.println("Id del item: " + invoice.getItem());
            System.out.println("Descripcion del producto: " + invoice.getDescription());
            System.out.println("Precio por unidad: " + invoice.getAmount());
            System.out.println("Unidades compradas: " + invoice.getInvoiceId().getTotalAmount()/invoice.getAmount());
            System.out.println("Monto total: " + invoice.getInvoiceId().getTotalAmount());
            System.out.println("-------------------------------");
        }
    }
 
    private void showAllCandidates() throws Exception{
        List<PartnerDto> candidates = this.service.getAllPartners("in progress");
        for (PartnerDto candidate : candidates){
            System.out.println("***Candidatos a VIP***");
            System.out.println("Id " + candidate.getId());
            System.out.println("Nombre de usuario: " + candidate.getUserId().getUserName());
            System.out.println("Nombre: " + candidate.getUserId().getPersonId().getName());
            System.out.println("Celular: " + candidate.getUserId().getPersonId().getCellPhone());
            System.out.println("Fecha de creacion: " + candidate.getCreationDate());
            System.out.println("Fondos disponibles: " + candidate.getAmount());
            System.out.println("Cantidad de facturas pagadas: " + this.service.getNumPaidInvoices(candidate));
        }
    }
            
    private void showAllVips() throws Exception{
        List<PartnerDto> vips = this.service.getAllPartners("vip");
        if (vips.isEmpty()){
            System.out.println("No se encontraron socios VIP activos");
        }
        else{System.out.println("***Socios Vip**");}
        
        for (PartnerDto vip : vips){
            System.out.println("Id " + vip.getId());
            System.out.println("Nombre de usuario: " + vip.getUserId().getUserName());
            System.out.println("Nombre: " + vip.getUserId().getPersonId().getName());
            System.out.println("Celular: " + vip.getUserId().getPersonId().getCellPhone());
            System.out.println("Fecha de creacion: " + vip.getCreationDate());
            System.out.println("Fondos disponibles: " + vip.getAmount());
            System.out.println("-------------------------");
        }
    }
    
    private void promoteToVip() throws Exception{
        this.showAllVips();
        this.showAllCandidates();
        
        System.out.println("Ingrese el id del socio que va a promover a VIP");
        Long id = adminValidator.validId(Utils.getReader().nextLine());
        
        PartnerDto candidate = validCandidate(id);
        
        if(candidate == null) throw new Exception("ERROR! No se encontro el candidato con ese ID");
        
        if (this.service.getNumPaidInvoices(candidate) == 0)throw new Exception("ERROR! El socio no tiene facturas pagadas.");

        this.service.promoteToVip(candidate);
    }
    
    private PartnerDto validCandidate(Long id) throws Exception{
        List<PartnerDto> candidates = this.service.getAllPartners("in progress");
        for(PartnerDto selectedCandidate : candidates) {
            if(selectedCandidate.getId() == id) return selectedCandidate;   
        }
        return null;
    }
}
