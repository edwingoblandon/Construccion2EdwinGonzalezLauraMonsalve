
package app.controller.validator;


public class GuestValidator extends CommonsValidator{
    //Explicit constructor is not needed
    
    public long validId(String id) throws Exception{
        return super.isValidLong("El id del invitado ", id);
    }
}
