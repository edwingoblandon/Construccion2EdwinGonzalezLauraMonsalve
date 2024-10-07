package app.controller;

import app.controller.validator.DetailInvoiceValidator;
import app.controller.validator.InvoiceValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.dto.DetailInvoiceDto;
import app.dto.InvoiceDto;
import app.dto.ProductDto;
import app.service.ClubService;
import app.service.interfaces.GuestService;
import app.service.interfaces.InvoiceService;
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
public class GuestController implements ControllerInterface{
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private InvoiceValidator invoiceValidator;
    @Autowired
    private DetailInvoiceValidator detailInvoiceValidator;
    @Autowired
    private GuestService service;
    @Autowired
    private ProductService productService;
    @Autowired
    private InvoiceService invoiceService;
    
    private static final String MENU = "Ingrese el numero de la opcion\n1. Hacer consumos\n2. Convertirse en socio \n3. Cerrar sesion";
    
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
            case "1":
                this.makePurchase();
                return true;
            case "2":{ 
                return convertGuestToPartner();
            }
            case "3":{
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
        
        
        
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setDateOfCreation(LocalDateTime.now());
        invoiceDto.setStatus("Pending");
        invoiceDto.setTotalAmount(productDto.getPrice()*cant);
        
        DetailInvoiceDto detailInvoiceDto = new DetailInvoiceDto();
        detailInvoiceDto.setDescription(productDto.getDescription());
        detailInvoiceDto.setAmount(productDto.getPrice());
        detailInvoiceDto.setItem((int) productDto.getId());
        detailInvoiceDto.setInvoiceId(invoiceDto);
        
        this.invoiceService.createInvoice(invoiceDto, detailInvoiceDto);
        System.out.println("Compra realizada con Exito");
    }
}
