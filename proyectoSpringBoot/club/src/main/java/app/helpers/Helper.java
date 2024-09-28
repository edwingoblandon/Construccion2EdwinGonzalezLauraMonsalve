
package app.helpers;

import app.dto.DetailInvoiceDto;
import app.dto.GuestDto;
import app.dto.InvoiceDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.dto.PartnerDto;
import app.model.DetailInvoice;
import app.model.Guest;
import app.model.Invoice;
import app.model.Person;
import app.model.User;
import app.model.Partner;
       
public abstract class Helper {
    
    public static PersonDto parse(Person person){
        PersonDto personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setDocument(person.getDocument());
        personDto.setName(person.getName());
        personDto.setCellPhone(person.getCellPhone());
        return personDto;
    }
    
    public static Person parse(PersonDto personDto){
        Person person = new Person();
        person.setId(personDto.getId());
        person.setDocument(personDto.getDocument());
        person.setName(personDto.getName());
        person.setCellPhone(personDto.getCellPhone());
        return person;
    }
    
	
    public static UserDto parse(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setPassword(user.getPassword());
        userDto.setPersonId(parse(user.getPersonId()));
        userDto.setRole(user.getRole());
        userDto.setUserName(user.getUserName());
        return userDto;
    }

    public static User parse(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        user.setPersonId(parse(userDto.getPersonId()));
        user.setRole(userDto.getRole());
        user.setUserName(userDto.getUserName());
        return user;
    }
    
    public static PartnerDto parse(Partner partner){
        PartnerDto partnerDto = new PartnerDto();
        partnerDto.setId(partner.getId());
        partnerDto.setUserId(parse(partner.getUserId()));
        partnerDto.setType(partner.getType());
        partnerDto.setCreationDate(partner.getCreationDate());
        partnerDto.setAmount(partner.getAmount());
        return partnerDto;
    }

    public static Partner parse(PartnerDto partnerDto){
        Partner partner = new Partner();
        partner.setId(partnerDto.getId());
        partner.setUserId(parse(partnerDto.getUserId()));
        partner.setType(partnerDto.getType());
        partner.setCreationDate(partnerDto.getCreationDate());
        partner.setAmount(partnerDto.getAmount());
        return partner;
    }
    
    public static GuestDto parse(Guest guest){
        GuestDto guestDto = new GuestDto();
        guestDto.setId(guest.getId());
        guestDto.setStatus(guest.getStatus());
        guestDto.setUserId(parse(guest.getUserId()));
        guestDto.setPartnerId(parse(guest.getPartnerId()));
        return guestDto;
    }

    public static Guest parse(GuestDto guestDto){
        Guest guest = new Guest();
        guest.setId(guestDto.getId());
        guest.setStatus(guestDto.getStatus());
        guest.setUserId(parse(guestDto.getUserId()));
        guest.setPartnerId(parse(guestDto.getPartnerId()));
        return guest;
    }
    
    public static InvoiceDto parse(Invoice invoice){
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(invoice.getId());
        invoiceDto.setDateOfCreation(invoice.getCreationDate());
        invoiceDto.setStatus(invoice.getStatus());
        invoiceDto.setTotalAmount(invoice.getTotalAmount());
        invoiceDto.setPartnerId(parse(invoice.getPartnerId()));
        invoiceDto.setUserId(parse(invoice.getUserId()));
        return invoiceDto;
    }
    
    public static Invoice parse(InvoiceDto invoiceDto){
        Invoice invoice = new Invoice();
        invoice.setId(invoiceDto.getId());
        invoice.setCreationDate(invoiceDto.getDateOfCreation());
        invoice.setStatus(invoiceDto.getStatus());
        invoice.setTotalAmount(invoiceDto.getTotalAmount());
        invoice.setPartnerId(parse(invoiceDto.getPartnerId()));
        invoice.setUserId(parse(invoiceDto.getUserId()));
        return invoice;
    }
    
    public static DetailInvoiceDto parse(DetailInvoice detailInvoice){
        DetailInvoiceDto detailInvoiceDto = new DetailInvoiceDto();
        detailInvoiceDto.setId(detailInvoice.getId());
        detailInvoiceDto.setDescription(detailInvoice.getDescription());
        detailInvoiceDto.setItem(detailInvoice.getItem());
        detailInvoiceDto.setAmount(detailInvoice.getAmount());
        detailInvoiceDto.setInvoiceId(parse(detailInvoice.getInvoiceId()));
        return detailInvoiceDto;
    }
    
    public static DetailInvoice parse(DetailInvoiceDto detailInvoiceDto){
        DetailInvoice detailInvoice = new DetailInvoice();
        detailInvoice.setId(detailInvoiceDto.getId());
        detailInvoice.setDescription(detailInvoiceDto.getDescription());
        detailInvoice.setItem(detailInvoiceDto.getItem());
        detailInvoice.setAmount(detailInvoiceDto.getAmount());
        detailInvoice.setInvoiceId(parse(detailInvoiceDto.getInvoiceId()));
        return detailInvoice;
    }

}
