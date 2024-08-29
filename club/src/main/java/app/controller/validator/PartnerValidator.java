
package app.controller.validator;

public class PartnerValidator extends CommonsValidator{
    public void validId(String id) throws Exception{
        super.isValidLong("El id de socio ", id);
    }
    
    public void validAmount(String amount) throws Exception{
        super.isValidDouble("El monto de socio ", amount);
    }
}
