
package app.controller.validator;


public class GuestValidator extends CommonsValidator{
    //Explicit constructor is not needed
    
    public void validId(String id) throws Exception{
        super.isValidLong("El id del invitado ", id);
    }
}
