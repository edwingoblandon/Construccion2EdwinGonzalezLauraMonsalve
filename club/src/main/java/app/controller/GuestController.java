package app.controller;

import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.service.Service;
import app.service.interfaces.GuestService;

public class GuestController implements ControllerInterface{
    private PersonValidator personValidator;
    private UserValidator userValidator;
    private GuestService service;
    private static final String MENU = "Ingrese el numero de la opcion\n1. convertirse en socio \n2. Cerrar sesion\n";
    
    
    
    @Override
    public void session() throws Exception {
        boolean session = true;
        while (session) {
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
            case "1":{ //Convertirse en socio desarrollar
                return true;
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
}
