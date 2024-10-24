package app.controller;

import app.controller.request.CreationUserRequest;
import app.controller.request.UpdateUserRequest;
import app.controller.response.InvoiceResponse;
import app.controller.response.PartnerResponse;
import app.controller.validator.AdminValidator;
import app.controller.validator.PartnerValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.dto.DetailInvoiceDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.service.interfaces.AdminService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Getter
@Setter
@NoArgsConstructor
@RestController()
public class AdminController implements ControllerInterface{

    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private PartnerValidator partnerValidator;
    @Autowired
    private AdminValidator adminValidator;
    @Autowired
    private AdminService service;

    @Override
    public void session() throws Exception {
    }

    @GetMapping("/")
    public String vive() {
        return "vive";
    }

    @PostMapping("/partner")
    private ResponseEntity createPartner(@RequestBody CreationUserRequest request) {
        try {
            String name = request.getName();
            personValidator.validName(name); //Void method
            long document = personValidator.validDocument(request.getDocument()); //Long method
            long celphone = personValidator.validCellPhone(request.getCellphone());
            String userName = request.getUsername();
            userValidator.validUserName(userName);
            String password = request.getPassword();
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

            return new ResponseEntity<>("socio creado Exitosamente", HttpStatus.OK);
        } catch (Exception e) {
             return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/invoices")
    private ResponseEntity<List<InvoiceResponse>> showAllInvoices() throws Exception {
        try{
            List<DetailInvoiceDto> invoices = this.service.getAllDetailInvoices();
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
            
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/candidates")
    private ResponseEntity<List<PartnerResponse>> showAllCandidates() throws Exception {
        try{
            List<PartnerDto> candidates = this.service.getAllPartners("in progress");
            List<PartnerResponse> responseList = new ArrayList<>();
            
            for (PartnerDto candidate : candidates){
                PartnerResponse partnerResponse = new PartnerResponse();
                
                partnerResponse.setId(candidate.getId());
                partnerResponse.setUserName(candidate.getUserId().getUserName());
                partnerResponse.setName(candidate.getUserId().getPersonId().getName());
                partnerResponse.setCellPhone(candidate.getUserId().getPersonId().getCellPhone());
                partnerResponse.setCreationDate(candidate.getCreationDate());
                partnerResponse.setAmount(candidate.getAmount());
                partnerResponse.setInvoicesPaid(this.service.getNumPaidInvoices(candidate));
                
                responseList.add(partnerResponse);
            }
            
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(List.of(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/vips")
    private ResponseEntity<List<PartnerResponse>> showAllVips() throws Exception {
        try{
            List<PartnerDto> vips = this.service.getAllPartners("vip");
            List<PartnerResponse> responseList = new ArrayList<>();
            
            for (PartnerDto vip : vips) {
                PartnerResponse partnerResponse = new PartnerResponse();
                
                partnerResponse.setId(vip.getId());
                partnerResponse.setUserName(vip.getUserId().getUserName());
                partnerResponse.setName(vip.getUserId().getPersonId().getName());
                partnerResponse.setCellPhone(vip.getUserId().getPersonId().getCellPhone());
                partnerResponse.setCreationDate(vip.getCreationDate());
                partnerResponse.setAmount(vip.getAmount());
                partnerResponse.setInvoicesPaid(this.service.getNumPaidInvoices(vip));
                
                responseList.add(partnerResponse);
            }
            
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch(Exception e){ 
            return new ResponseEntity<>(List.of(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/promote")
    private ResponseEntity promoteToVip(@RequestBody UpdateUserRequest request) throws Exception {
        try{
            long id = userValidator.validId(request.getId());
            PartnerDto candidate = validCandidate(id);

             if (candidate == null) {
                return new ResponseEntity<>("ERROR! No se encontró el candidato con ese ID", HttpStatus.NOT_FOUND);
            }

            if (this.service.getNumPaidInvoices(candidate) == 0) {
                return new ResponseEntity<>("ERROR! El socio no tiene facturas pagadas.", HttpStatus.BAD_REQUEST);
            }

            this.service.promoteToVip(candidate);
            return new ResponseEntity<>("El socio ha sido promovido a VIP con exito", HttpStatus.OK);
         } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private PartnerDto validCandidate(Long id) throws Exception {
        List<PartnerDto> candidates = this.service.getAllPartners("in progress");
        for (PartnerDto selectedCandidate : candidates) {
            if (selectedCandidate.getId() == id) {
                return selectedCandidate;
            }
        }
        return null;
    }
}
