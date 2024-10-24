package app.controller;

import app.controller.request.CreationInvoiceRequest;
import app.controller.request.CreationUserRequest;
import app.controller.request.RechargeRequest;
import app.controller.request.UpdateUserRequest;
import app.controller.request.DeleteUserRequest;
import app.controller.response.InvoiceResponse;
import app.controller.validator.DetailInvoiceValidator;
import app.controller.validator.GuestValidator;
import app.controller.validator.InvoiceValidator;
import app.controller.validator.PartnerValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.dto.DetailInvoiceDto;
import app.dto.GuestDto;
import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.ProductDto;
import app.dto.UserDto;
import app.service.interfaces.InvoiceService;
import app.service.interfaces.PartnerService;
import app.service.interfaces.ProductService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Getter
@Setter
@NoArgsConstructor
@RestController
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

    private static final String MENU = "Ingrese la el numero de la opcion\n1. Hacer consumos\n2. Crear invitado \n3. Activar invitado\n4. Desactivar invitado\n5. Recargar fondos\n6. Solicitar VIP\n7. Solicitar Baja\n8. Ver facturas\n9. Cerrar sesion";

    @Override
    public void session() throws Exception {

    }

    @PostMapping("/guest")
    private ResponseEntity createGuest(@RequestHeader("userid") String userId, @RequestBody CreationUserRequest request) {
        try {
            String name = request.getName();
            personValidator.validName(name); //Void method
            long document = personValidator.validDocument(request.getDocument()); //Long method
            long cellphone = personValidator.validCellPhone(request.getCellphone());
            String userName = request.getUsername();
            userValidator.validUserName(userName);
            String password = request.getPassword();
            userValidator.validPassword(password);
            long sessionUserId = userValidator.validId(userId);

            PersonDto personDto = new PersonDto();
            personDto.setName(name);
            personDto.setDocument(document);
            personDto.setCellPhone(cellphone);

            UserDto userDto = new UserDto();
            userDto.setPersonId(personDto);
            userDto.setUserName(userName);
            userDto.setPassword(password);
            userDto.setRole("guest");

            PartnerDto partnerDto = new PartnerDto();
            partnerDto.setUserId(new UserDto());
            partnerDto.getUserId().setId(sessionUserId);

            GuestDto guestDto = new GuestDto();
            guestDto.setUserId(userDto);
            guestDto.setStatus("Inactive");
            guestDto.setPartnerId(partnerDto);

            this.service.createGuest(guestDto);
            return new ResponseEntity<>("se ha creado el invitado exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/consume/partner")
    private ResponseEntity makePurchase(@RequestHeader("userid") String userId, @RequestBody CreationInvoiceRequest request) throws Exception {
        try{
            List<ProductDto> products = productService.getAllProducts();

            System.out.println("***Productos disponibles***");
            int numProducts = 0;
            for (ProductDto productDto : products) {
                System.out.println("ID: " + productDto.getId());
                System.out.println("Nombre: " + productDto.getName());
                System.out.println("Descripcion: " + productDto.getDescription());
                System.out.println("Precio: $" + productDto.getPrice());
                System.out.println("-------------------------------");
                numProducts += 1;
            }

            Long id = invoiceValidator.validId(request.getId());
            if (id < 0 || id > numProducts) {
                throw new Exception("Ingreso una opcion invalida");
            }
            ProductDto productDto = products.get(id.intValue() - 1);

            int quantity = invoiceValidator.validQuantity(request.getQuantity());

            int paymentOption = invoiceValidator.validPaymentOption(request.getPaymentOption());

            if(paymentOption != 1 && paymentOption != 2) throw new Exception("Ingreso una opcion de pago invalida");

            InvoiceDto invoiceDto = new InvoiceDto();
            invoiceDto.setDateOfCreation(LocalDateTime.now());
            invoiceDto.setTotalAmount(productDto.getPrice() * quantity);
            String msg = "La factura quedo para pagar despues";
            if(paymentOption == 1){
                PartnerDto partnerDto = getPartnerDtoFromRequest(userId);
                boolean payment = this.validatePayment(partnerDto, invoiceDto);
                msg = (payment ? "La factura quedo pagada con exito" : "La factura quedo pendiente de pago porque sus fondos no son suficientes");
                invoiceDto.setStatus(payment ? "Paid" : "Pending");
            }
            
            DetailInvoiceDto detailInvoiceDto = new DetailInvoiceDto();
            detailInvoiceDto.setDescription(productDto.getDescription());
            detailInvoiceDto.setAmount(productDto.getPrice());
            detailInvoiceDto.setItem((int) productDto.getId());
            detailInvoiceDto.setInvoiceId(invoiceDto);
            
            long sessionUserId = userValidator.validId(userId);
            UserDto userDto = new UserDto();
            userDto.setId(sessionUserId);
            
            this.invoiceService.createInvoice(userDto, invoiceDto, detailInvoiceDto);
            return new ResponseEntity<>("Compra realizada con Exito" + msg,HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/activate")
    private ResponseEntity activateGuest(@RequestHeader("userid") String userId, @RequestBody UpdateUserRequest request) throws Exception {
        try{
            long guestId = guestValidator.validId(request.getId());
            
            PartnerDto partnerDto = getPartnerDtoFromRequest(userId);
            
            GuestDto guestDto = new GuestDto();
            guestDto.setId(guestId);
            guestDto.setPartnerId(partnerDto);

            service.activateGuest(guestDto);

            return new ResponseEntity<>("El invitado ha sido activado exitosamente", HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/inactivate")
    private ResponseEntity inactivateGuest(@RequestHeader("userid") String userId, @RequestBody UpdateUserRequest request) throws Exception {
        try{
            long guestId = guestValidator.validId(request.getId());
            
            PartnerDto partnerDto = getPartnerDtoFromRequest(userId);
                    
            GuestDto guestDto = new GuestDto();
            guestDto.setId(guestId);
            guestDto.setPartnerId(partnerDto);

            service.inactivateGuest(guestDto);
            
            return new ResponseEntity<>("El invitado ha sido desactivado exitosamente.", HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void showGuestList(List<GuestDto> guests, String message) {
        System.out.println("\n" + message);
        for (GuestDto guest : guests) {
            System.out.println("ID: " + guest.getId());
            System.out.println("Nombre: " + guest.getUserId().getPersonId().getName());
            System.out.println("-------------------------------");
        }
    }

    @GetMapping("/invoices/partner")
    private ResponseEntity<Object> showAllInvoices(@RequestHeader("userid") String userId) throws Exception {
        try{
            PartnerDto partnerDto = getPartnerDtoFromRequest(userId);
            
            List<DetailInvoiceDto> invoices = this.service.getAllDetailInvoiceByPartner(partnerDto);
            List<InvoiceResponse> responseList = new ArrayList<>();
            
            for (DetailInvoiceDto invoice : invoices){
                InvoiceResponse invoiceResponse = new InvoiceResponse();
                
                invoiceResponse.setId(invoice.getId());
                invoiceResponse.setStatus(invoice.getInvoiceId().getStatus().equalsIgnoreCase("Pending") ? "Pendiente" : "Pagada");
                invoiceResponse.setUserName(invoice.getInvoiceId().getUserId().getUserName());
                invoiceResponse.setUserType(invoice.getInvoiceId().getUserId().getRole().equalsIgnoreCase("Partner") ? "Socio" : "Invitado");
                invoiceResponse.setDateOfCreation(invoice.getInvoiceId().getDateOfCreation());
                invoiceResponse.setItemId(invoice.getItem());
                invoiceResponse.setDescription(invoice.getDescription());
                invoiceResponse.setUnitPrice(invoice.getAmount());
                invoiceResponse.setQuantity((int) (invoice.getInvoiceId().getTotalAmount() / invoice.getAmount()));
                invoiceResponse.setTotalAmount(invoice.getInvoiceId().getTotalAmount());
                
                responseList.add(invoiceResponse);
            }
            
            return new ResponseEntity<>(responseList,HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/unsuscriberequest")
    private ResponseEntity unsubscribeRequest(@RequestHeader("userid") String userId, @RequestBody DeleteUserRequest request) throws Exception {
        try{
            PartnerDto partnerDto = getPartnerDtoFromRequest(userId);
            
            String confirmation = request.getConfirmation();
            
            if (confirmation.equals("SI")) {
                service.unsubscribeRequest(partnerDto);
                return new ResponseEntity<>("Se te dio de baja con Exito",HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Se cancelo tu solicitud de desuscripcion",HttpStatus.OK);
            }
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/viprequest")
    private ResponseEntity vipPromotionRequest(@RequestHeader("userid") String userId, @RequestBody UpdateUserRequest request) throws Exception {
        try{
            PartnerDto partnerDto = getPartnerDtoFromRequest(userId);
            String confirmation = request.getConfirmation();
            
            if (confirmation.equals("SI")) {
                service.vipPromotionRequest(partnerDto);
                return new ResponseEntity<>("Se ha enviado su solicitud para promoverlo a VIP",HttpStatus.OK);
            }
            return new ResponseEntity<>("Se cancelo tu solicitud", HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/recharge")
    private ResponseEntity increaseFunds(@RequestHeader("id") String id,@RequestBody RechargeRequest request) throws Exception {
        try{
            PartnerDto partnerDto = new PartnerDto();
            partnerDto.setId(partnerValidator.validId(id));
            
            double amount = partnerValidator.validAmount(request.getAmount());
            service.increaseFunds(amount,partnerDto);
            return new ResponseEntity<>("Recarga realizada con exito",HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    private PartnerDto getPartnerDtoFromRequest(String userId) throws Exception {
        long sessionUserId = userValidator.validId(userId);
        PartnerDto partnerDto = new PartnerDto();
        partnerDto.setUserId(new UserDto());
        partnerDto.getUserId().setId(sessionUserId);
        return partnerDto;
    }
    
    private boolean validatePayment(PartnerDto partnerDto, InvoiceDto invoiceDto) throws Exception{
        return this.service.payInvoice(partnerDto, invoiceDto);
    }
}
