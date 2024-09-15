
package app.controller.validator;

public class InvoiceValidator extends CommonsValidator {
    
    public void validId(String id) throws Exception{
        super.isValidLong("El id de la factura ", id);
    }
    
    public void validTotalAmount(String value) throws Exception{
        super.isValidDouble("El monto total ", value);
    }
}
