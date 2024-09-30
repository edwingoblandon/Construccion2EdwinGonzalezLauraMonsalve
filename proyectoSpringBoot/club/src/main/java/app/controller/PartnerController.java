package app.controller;

import app.controller.validator.DetailInvoiceValidator;
import app.controller.validator.GuestValidator;
import app.controller.validator.InvoiceValidator;
import app.controller.validator.PartnerValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.dto.DetailInvoiceDto;
import app.dto.GuestDto;
import app.dto.InvoiceDto;
import app.dto.PersonDto;
import app.dto.ProductDto;
import app.dto.UserDto;
import app.service.ClubService;
import app.service.interfaces.InvoiceService;
import app.service.interfaces.PartnerService;
import app.service.interfaces.ProductService;
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
    private InvoiceValidator invoiceValidator;
    @Autowired
    private DetailInvoiceValidator detailInvoiceValidator;
    
    @Autowired
    private PartnerService service;
    @Autowired
    private ProductService productService;
    @Autowired
    private InvoiceService invoiceService;
    
    private static final String MENU = "Ingrese la el numero de la opcion\n1. Hacer consumos\n2. Crear invitado \n3. Activar invitado\n4. Desactivar invitado\n5. Recargar fondos\n6. Solicitar VIP\n7. Solicitar Baja\n8. Cerrar sesion";
    
    
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
                this.makePurchase();
                return true;
            case "2":
                this.createGuest();
                return true;
            case "3":
                this.activateGuest();
                return true;
            case "4":
                this.inactivateGuest();
                return true;
            case "5":
                this.increaseFunds();
                return true;
            case "6":
                this.vipPromotionRequest();
                return true;
            case "7":
                return this.unsubscribeRequest();
            case "8":{
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
    
    private void makePurchase() throws Exception{
        List<ProductDto> products = productService.getAllProducts();
        
        System.out.println("***Productos disponibles***");
        System.out.println("-------------------------------");
        
        int numProducts = 0;
        for (ProductDto productDto : products){
            System.out.println("ID: " + productDto.getId());
            System.out.println("Nombre: " + productDto.getName());
            System.out.println("Descripcion: " + productDto.getDescription());
            System.out.println("Precio: $" + productDto.getPrice());
            System.out.println("-------------------------------");
            numProducts += 1;
        }
        
        System.out.println("\nIngrese el ID del producto que desea");
        Long id = invoiceValidator.validId(Utils.getReader().nextLine());
        if(id < 0 || id >= numProducts) throw new Exception("Ingrese una opcion valida");
        ProductDto productDto = products.get(id.intValue() - 1);
        
        System.out.println("Ingrese la cantidad de: " + productDto.getName() + " que desea: " );
        int cant = Integer.parseInt(Utils.getReader().nextLine());
       
        int opt;
        do {
            System.out.println("Desea pagar ahora o después? (Escriba 1 para pagar ahora, 2 para pagar despues):");
            opt = invoiceValidator.validStatus(Utils.getReader().nextLine());
        } while (opt != 1 && opt != 2);
        
        
        
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setDateOfCreation(LocalDateTime.now());
        int status = opt == 1 ? 1 : 2;
        invoiceDto.setStatus(status == 1 ? "Paid" : "Pending");
        invoiceDto.setTotalAmount(productDto.getPrice()*cant);
        
        DetailInvoiceDto detailInvoiceDto = new DetailInvoiceDto();
        detailInvoiceDto.setDescription(productDto.getDescription());
        detailInvoiceDto.setAmount(productDto.getPrice());
        detailInvoiceDto.setItem((int) productDto.getId());
        detailInvoiceDto.setInvoiceId(invoiceDto);
        
        this.invoiceService.createInvoice(invoiceDto, detailInvoiceDto);
        System.out.println("Compra realizada con Exito");
    }
    
    
    private void activateGuest() throws Exception{
        List<GuestDto> inactiveGuests = service.getGuestsForPartnerSession("Inactive");
        
        showGuestList(inactiveGuests, "Invitados inactivos del socio en sesion:");
        
        System.out.println("Ingrese el ID del invitado que desea activar");
        long guestId = Long.parseLong(Utils.getReader().nextLine());
        GuestDto guestDto = new GuestDto();
        guestDto.setId(guestId);
        service.activateGuest(guestDto);

        System.out.println("El invitado ha sido activado exitosamente.");
    }
    
    private void inactivateGuest() throws Exception {
        List<GuestDto> activeGuests = service.getGuestsForPartnerSession("Active");
        
        showGuestList(activeGuests, "Invitados activos del socio en sesion:");
        
        System.out.println("Ingrese el ID del invitado que desea desactivar");
        long guestId = guestValidator.validId(Utils.getReader().nextLine());
        
        GuestDto guestDto = new GuestDto();
        guestDto.setId(guestId);

        service.inactivateGuest(guestDto);

        System.out.println("El invitado ha sido desactivado exitosamente.");
    }
    
    private void showGuestList(List<GuestDto> guests, String message) {
        System.out.println("\n" + message);
        for (GuestDto guest : guests) {
            System.out.println("ID: " + guest.getId());
            System.out.println("Nombre: " + guest.getUserId().getPersonId().getName());
            System.out.println("-------------------------------");
        }
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
    
    private void vipPromotionRequest() throws Exception{
        System.out.println("Estas seguro de realizar la solicitud a VIP?. Para continuar escriba SI, para cancelar persiona ENTER.");
        String opt = Utils.getReader().nextLine().toUpperCase();
        
        if (opt.equalsIgnoreCase("SI")){
            service.vipPromotionRequest();
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
