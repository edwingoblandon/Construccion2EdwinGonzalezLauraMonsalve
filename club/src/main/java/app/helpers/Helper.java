
package app.helpers;

import app.dto.PersonDto;
import app.dto.UserDto;
import app.dto.PartnerDto;
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
    
}
