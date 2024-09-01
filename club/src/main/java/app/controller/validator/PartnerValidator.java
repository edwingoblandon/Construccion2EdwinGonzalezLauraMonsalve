
package app.controller.validator;

public class PartnerValidator extends CommonsValidator{
    
    public double validAmount(String type, String amount) throws Exception{
        double validAmount = super.isValidDouble("El monto del socio", amount);
        if (validAmount < 0 || validAmount > 5000000) throw new Exception("El monto debe ser mayor o igual a 0 y menor a 5000000");
        
        return validAmount;
    }
    
    public void validType(String type) throws Exception{
        if (!type.equalsIgnoreCase("VIP") && !type.equalsIgnoreCase("REGULAR")) throw new Exception("El tipo de socio debe ser VIP o REGULAR");
        super.isValidString("El tipo de socio ", type);
    }
}
