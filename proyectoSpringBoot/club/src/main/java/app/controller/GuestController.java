package app.controller;

import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.service.ClubService;
import app.service.interfaces.GuestService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Getter
@Setter
@NoArgsConstructor
@Controller
public class GuestController implements ControllerInterface{
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private GuestService service;
    private static final String MENU = "Ingrese el numero de la opcion\n1. convertirse en socio \n2. Cerrar sesion";
    
    @Override
    public void session() throws Exception {
        boolean session = true;
        while (session) {
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
            case "1":{ 
                return convertGuestToPartner();
            }
            case "2":{
                System.out.println("Se ha cerrado sesion con exito.");
                return false;
            }
            default: {
                System.out.println("Opcion incorrecta, intentelo de nuevo");
                return true;
            }
        }
    }
    
    private boolean convertGuestToPartner() throws Exception{
        System.out.println("Estas seguro de convertirte en socio?. Para continuar escriba SI, para cancelar persiona ENTER.");
        String opt = Utils.getReader().nextLine().toUpperCase();
        
        if (opt.equals("SI")) {
            service.convertGuestToPartner();
            System.out.println("Proceso realizado con exito!\nSe ha cerrado la sesion, vuelve a iniciar sesion");
            return false;
        }
        System.out.println("Cancelacion del proceso con exito");
        return true;
    }
}
