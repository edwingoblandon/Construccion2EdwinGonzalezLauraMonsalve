package app.controller;

import app.controller.validator.GuestValidator;
import app.controller.validator.PartnerValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.dto.GuestDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.service.ClubService;
import app.service.interfaces.PartnerService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Getter
@Setter
@NoArgsConstructor
@Controller
public class PartnerController implements ControllerInterface {
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private GuestValidator guestValidator;
    @Autowired
    private PartnerValidator partnerValidator;
    @Autowired
    private PartnerService service;
    private static final String MENU = "Ingrese la el numero de la opcion\n1. Crear invitado \n2. Activar invitado\n3. Desactivar el invitado\n4. Recargar fondos\n5. Solicitar VIP\n6. Solicitar Baja\n7. Cerrar sesion";
    
    
    @Override
    public void session() throws Exception {
        boolean session = true;
        while(session){
            session = menu();
        }
    }
    
    private boolean menu(){
        try{
            System.out.println("bienvenido(a) " + ClubService.user.getUserName());
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
            case "2":
                this.activateGuest();
                return true;
            case "3":
                this.inactivateGuest();
                return true;
            case "4":
                this.increaseFunds();
                return true;
            case "5":
                this.VipPromotionRequest();
                return true;
            case "6":
                return this.unsubscribeRequest();
            case "7":{
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

        GuestDto guestDto = new GuestDto();
        guestDto.setUserId(userDto);
        guestDto.setStatus("Inactive");
        
        this.service.createGuest(guestDto);
        System.out.println("Se ha creado el usuario exitosamente");
    }
    
    private void activateGuest() throws Exception{
        service.showGuestsForPartnerSession("Inactive");
        System.out.println("Ingrese el ID del invitado que desea activar");
        long guestId = Long.parseLong(Utils.getReader().nextLine());
        GuestDto guestDto = new GuestDto();
        guestDto.setId(guestId);
        service.activateGuest(guestDto);

        System.out.println("El invitado ha sido activado exitosamente.");
    }
    
    private void inactivateGuest() throws Exception {
        service.showGuestsForPartnerSession("Active");
        System.out.println("Ingrese el ID del invitado que desea desactivar");
        long guestId = guestValidator.validId(Utils.getReader().nextLine());
        
        GuestDto guestDto = new GuestDto();
        guestDto.setId(guestId);

        service.inactivateGuest(guestDto);

        System.out.println("El invitado ha sido desactivado exitosamente.");
    }
    
    private boolean unsubscribeRequest() throws Exception{
        System.out.println("Estas seguro de eliminar tu usuario?. Para continuar escriba SI, para cancelar persiona ENTER.");
        String opt = Utils.getReader().nextLine().toUpperCase();
        
        if (opt.equals("SI")) {
            service.unsubscribeRequest();
            System.out.println("Se te ha dado de baja!");
            return false;
        }
        return true;
    }
    
    private void VipPromotionRequest() throws Exception{
        System.out.println("Estas seguro de realizar la solicitud a VIP?. Para continuar escriba SI, para cancelar persiona ENTER.");
        String opt = Utils.getReader().nextLine().toUpperCase();
        
        if (opt.equalsIgnoreCase("SI")){
            service.VipPromotionRequest();
            System.out.println("Se ha enviado su solicitud para promoverlo a VIP");
        } 
    }
    
    private void increaseFunds() throws Exception{
        System.out.println("Ingrese la cantidad de fondos que desea recargar");
        double amount = partnerValidator.validAmount(Utils.getReader().nextLine());
        service.increaseFunds(amount);
        System.out.println("Se recargo los fondos con exito!");
    }
}
