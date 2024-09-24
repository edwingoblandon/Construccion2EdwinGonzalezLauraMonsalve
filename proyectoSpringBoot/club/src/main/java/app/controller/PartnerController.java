package app.controller;

import app.controller.validator.GuestValidator;
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
            case "2": //activate guest 
                this.activateGuest();
                return true;
            case "3": //inactivate guest
                this.inactivateGuest();
                return true;
            case "4":
            case "5":
                return true;
            case "6":
                this.requestToUnsubscribe();
                return true;
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
        guestDto.setStatus("active");
        
        this.service.createGuest(guestDto);
        System.out.println("Se ha creado el usuario exitosamente");
    }
    
    private void activateGuest() throws Exception{
        System.out.println("Ingrese el ID del invitado que desea activar");
        long guestId = Long.parseLong(Utils.getReader().nextLine());
        GuestDto guestDto = new GuestDto();
        guestDto.setId(guestId);
        service.activateGuest(guestDto);

        System.out.println("El invitado ha sido activado exitosamente.");
    }
    
    private void inactivateGuest() throws Exception {
        System.out.println("Ingrese el ID del invitado que desea desactivar");
        long guestId = guestValidator.validId(Utils.getReader().nextLine());
        
        GuestDto guestDto = new GuestDto();
        guestDto.setId(guestId);

        service.inactivateGuest(guestDto);

        System.out.println("El invitado ha sido desactivado exitosamente.");
        
    }
    
    private void requestToUnsubscribe() throws Exception{
        System.out.println("Estas seguro de eliminar tu usuario?. Para continuar escriba SI, para cancelar persiona ENTER.");
        String opt = Utils.getReader().nextLine().toUpperCase();
        if (opt.equalsIgnoreCase("SI")) service.requestToUnsubscribe();
    }
    
}
