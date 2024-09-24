
package app.controller;

import app.controller.validator.PartnerValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.service.ClubService;
import app.service.interfaces.AdminService;
import java.time.LocalDateTime;
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
    private AdminService service;
    private static final String MENU = "Ingrese la el numero de la opcion \n1. Crear socio\n2. Ver facturas\n3. Promover a VIP\n4. Cerrar sesion \n";

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
            case "3":
                return true;
                //pass
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
 
}
