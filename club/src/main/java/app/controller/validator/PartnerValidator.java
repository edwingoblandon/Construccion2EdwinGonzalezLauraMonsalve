
package app.controller.validator;

public class PartnerValidator extends CommonsValidator{
    
    public double validAmount(String amount) throws Exception{
        return super.isValidDouble("El monto de socio ", amount);
    }
    
    public void validType(String type) throws Exception{
        if (!type.equalsIgnoreCase("VIP") && !type.equalsIgnoreCase("REGULAR")) throw new Exception("El tipo de socio debe ser VIP o REGULAR");
        super.isValidString("El tipo de socio ", type);
    }
}
