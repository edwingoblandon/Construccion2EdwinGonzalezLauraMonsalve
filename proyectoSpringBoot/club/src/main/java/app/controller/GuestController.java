package app.controller;

import app.controller.request.CreationInvoiceRequest;
import app.controller.request.UpdateUserRequest;
import app.controller.validator.DetailInvoiceValidator;
import app.controller.validator.InvoiceValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.dto.DetailInvoiceDto;
import app.dto.GuestDto;
import app.dto.InvoiceDto;
import app.dto.ProductDto;
import app.dto.UserDto;
import app.service.interfaces.GuestService;
import app.service.interfaces.InvoiceService;
import app.service.interfaces.ProductService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

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
    }
    
    @PutMapping("/convertToPartner")
    private ResponseEntity convertGuestToPartner(@RequestHeader("userid") String userId, @RequestBody UpdateUserRequest request ) throws Exception{
        try{
            long sessionUserId = userValidator.validId(userId);
            GuestDto guestDto = new GuestDto();
            guestDto.setUserId(new UserDto());
            guestDto.getUserId().setId(sessionUserId);
            
            String confirmation = request.getConfirmation();
            
            if (confirmation.equals("SI")) {
                service.convertGuestToPartner(guestDto);
                return new ResponseEntity<>("Proceso realizado con exito!",HttpStatus.OK);
            } 
            else{
                return new ResponseEntity<>("Cancelacion del proceso con exito",HttpStatus.OK);
            }
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/consume/guest")
    private ResponseEntity makePurchase(@RequestHeader("userid") String userId, @RequestBody CreationInvoiceRequest request) throws Exception {
        try{
            List<ProductDto> products = productService.getAllProducts();
            int numProducts = 0;
            for (ProductDto productDto : products) {
                numProducts += 1;
            }

            Long id = invoiceValidator.validId(request.getId());
            if (id < 0 || id > numProducts) {
                throw new Exception("Ingreso una opcion invalida");
            }
            ProductDto productDto = products.get(id.intValue() - 1);

            System.out.println("Ingrese la cantidad de: " + productDto.getName() + " que desea: ");
            int quantity = invoiceValidator.validQuantity(request.getQuantity());

            InvoiceDto invoiceDto = new InvoiceDto();
            invoiceDto.setDateOfCreation(LocalDateTime.now());
            invoiceDto.setTotalAmount(productDto.getPrice() * quantity);
            invoiceDto.setStatus("Pending");
            
            DetailInvoiceDto detailInvoiceDto = new DetailInvoiceDto();
            detailInvoiceDto.setDescription(productDto.getDescription());
            detailInvoiceDto.setAmount(productDto.getPrice());
            detailInvoiceDto.setItem((int) productDto.getId());
            detailInvoiceDto.setInvoiceId(invoiceDto);
            
            long sessionUserId = userValidator.validId(userId);
            UserDto userDto = new UserDto();
            userDto.setId(sessionUserId);
            
            this.invoiceService.createInvoice(userDto, invoiceDto, detailInvoiceDto);
            return new ResponseEntity<>("Compra realizada con Exito", HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
