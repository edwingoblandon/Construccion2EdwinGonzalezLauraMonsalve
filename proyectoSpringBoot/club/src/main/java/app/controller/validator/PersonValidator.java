package app.controller.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class PersonValidator extends CommonsValidator {
    
    //Explicit constructor is not needed
    public void validName(String name) throws Exception{
        super.isValidString("El nombre de la persona ", name);
    }
    
    public long validDocument(String document) throws Exception{
        return super.isValidLong("La cedula de la persona ", document);
    }
    
    public long validCellPhone(String celphone) throws Exception{
        if (celphone.length() != 10) throw new Exception("El numero de celular de la persona debe ser de 10 digitos");
        return super.isValidLong("El telefono de la persona", celphone);
    }
}