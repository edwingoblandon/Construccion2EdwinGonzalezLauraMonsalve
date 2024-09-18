
package app.controller.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class UserValidator extends CommonsValidator{
    
    public void validUserName(String userName) throws Exception{
        super.isValidString("El nombre de usuario ", userName);
    }
    
    public void validPassword(String password) throws Exception{
        super.isValidString("La contraseña de usuario ", password);
    }
    
    public void validRole(String Role) throws Exception{
        super.isValidString("El rol de usuario ", Role);
    }
}
