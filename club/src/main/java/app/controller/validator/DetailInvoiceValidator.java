
package app.controller.validator;

public class DetailInvoiceValidator extends CommonsValidator{
    
    public void validId(String id) throws Exception{
        super.isValidLong("El id de la factura detallada ", id);
    }
    
    public void validItems(String items) throws Exception{
        super.isValidInteger("Los items de la factura detallada ", items);
    }
    
    public void validAmount(String amount) throws Exception{
        super.isValidDouble("El monto de la factura detallada ", amount);
    }
    
    public void validDescription(String description) throws Exception{
        super.isValidString("La descripcion de la factura detallada ", description);
    }
}
