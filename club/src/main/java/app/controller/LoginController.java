package app.controller;
//Finish
import java.util.HashMap;
import java.util.Map;

import app.controller.validator.UserValidator;
import app.dto.UserDto;
import app.service.Service;
import app.service.interfaces.LoginService;

public class LoginController implements ControllerInterface { 
    
    
    private LoginService service;
    private UserValidator userValidator;
    private static final String MENU= "ingrese la opcion que desea: \n 1. para iniciar sesion. \n 2. para detener la ejecucion.";
    private Map<String,ControllerInterface> roles;
    
    public LoginController(){
        this.userValidator = new UserValidator();
        this.service = new Service();
        ControllerInterface adminController = new AdminController();
        ControllerInterface partnerController = new PartnerController();
        ControllerInterface guestController = new GuestController();
        this.roles = new HashMap<String,ControllerInterface>();
        roles.put("admin", adminController);
        roles.put("partner", partnerController);
        roles.put("guest", guestController);
    }
    
    @Override
    public void session() throws Exception {
        boolean session = true;
        while (session) {
                session = menu();
        }		
    }
    
    private boolean menu() {
        try {
            System.out.println(MENU);
            String option = Utils.getReader().nextLine();
            return options(option);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return true;
        }
    }
    
    private boolean options(String option) throws Exception{
        switch(option){
            case "1":{
                this.login();
                return true;
            }
            case "2":{
                System.out.println("Se detuvo el programa");
                return false;
            }
            default:{
                System.out.println("Ingrese una opcion valida");
                return true;
            }
        }
    }
    
    private void login()throws Exception {
        System.out.println("ingrese el usuario");
        String userName= Utils.getReader().nextLine();
        userValidator.validUserName(userName);
        System.out.println("ingrese la contraseña");
        String password= Utils.getReader().nextLine();
        userValidator.validPassword(password);
        UserDto userDto = new UserDto();
        userDto.setPassword(password);
        userDto.setUserName(userName);
        this.service.login(userDto);
        if(roles.get(userDto.getRole())==null) {
                throw new Exception ("Rol invalido");
        }
        roles.get(userDto.getRole()).session();
    }
}
