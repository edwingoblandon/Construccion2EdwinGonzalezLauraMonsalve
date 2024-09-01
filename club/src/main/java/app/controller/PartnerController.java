package app.controller;

import app.controller.validator.GuestValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.dto.GuestDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.service.Service;
import app.service.interfaces.PartnerService;
import java.time.LocalDateTime;

public class PartnerController implements ControllerInterface {
    private PersonValidator personValidator;
    private UserValidator userValidator;
    private GuestValidator guestValidator;
    private PartnerService service;
    private static final String MENU = "Ingrese la el numero de la opcion\n1. Crear invitado \n2. Activar invitado\n3. Desactivar el invitado\n4. Solicitar Baja\n5. Cerrar sesion";
    
    public PartnerController() {
        this.personValidator = new PersonValidator();
        this.userValidator = new UserValidator();
        this.guestValidator = new GuestValidator();
        this.service = new Service();
    }
    
    @Override
    public void session() throws Exception {
        boolean session = true;
        while(session){
            session = menu();
        }
    }
    
    private boolean menu(){
        try{
            System.out.println("bienvenido(a) " + Service.user.getUserName());
            System.out.println(MENU);
            String option = Utils.getReader().nextLine();
            return options(option);
        } catch(
                Exception e){
            System.out.println(e.getMessage());
            return true;
        }
    }
    
    private boolean options(String option) throws Exception{
        switch(option){
            case "1":
                this.createGuest();
                return true;
            case "2": //activate guest 
                
            case "3": //inactivate guest
                this.inactivateGuest();
            case "4"://request to unsubscribe
                return true;
            case "5":{
                System.out.println("Se ha cerrado sesion con exito.");
                return false;
            } 
            default: {
                System.out.println("Opcion incorrecta, intentelo de nuevo");
                return true;
            }
        }
    }
    
    private void createGuest() throws Exception{
        System.out.println("Ingrese el nombre del invitado");
        String name = Utils.getReader().nextLine(); 
        personValidator.validName(name); //Void method
        System.out.println("Ingrese el numero de documento del invitado");
        long document = personValidator.validDocument(Utils.getReader().nextLine()); //Long method
        System.out.println("Ingrese el numero de celular del invitado");
        long celphone = personValidator.validCellPhone(Utils.getReader().nextLine());
        System.out.println("Ingrese el nombre de usuario del invitado");
        String userName = Utils.getReader().nextLine();
        userValidator.validUserName(userName);
        System.out.println("Ingrese una contraseña para el invitado");
        String password = Utils.getReader().nextLine();
        userValidator.validPassword(password);
       
        PersonDto personDto = new PersonDto();
        personDto.setName(name);
        personDto.setDocument(document);
        personDto.setCellPhone(celphone);
        
        UserDto userDto = new UserDto();
        userDto.setPersonId(personDto);
        userDto.setUserName(userName);
        userDto.setPassword(password);
        userDto.setRole("guest");
        
        PartnerDto partnerDto = this.service.getSessionPartner();
        
        
        GuestDto guestDto = new GuestDto();
        guestDto.setUserId(userDto);
        guestDto.setStatus("active");
        guestDto.setPartnerId(partnerDto);
        
        this.service.createGuest(guestDto);
        System.out.println("Se ha creado el usuario exitosamente");
    }
    
    private void activateGuest() throws Exception{
        
    }
    
    private void inactivateGuest() throws Exception {
        
    }
    
}
