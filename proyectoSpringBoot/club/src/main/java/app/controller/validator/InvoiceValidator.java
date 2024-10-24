package app.controller.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class InvoiceValidator extends CommonsValidator {
    
    public Long validId(String id) throws Exception{
        return super.isValidLong("El id de la factura ", id);
    }
    
    public double validTotalAmount(String value) throws Exception{
        return super.isValidDouble("El monto total ", value);
    }
    
    public int validStatus(String status) throws Exception{
        return super.isValidInteger("La opcion de pago ", status);
    }
    
    public int validQuantity(String quantity) throws Exception{
        return super.isValidInteger("La cantidad ", quantity);
    }
    
    public int validPaymentOption(String option) throws Exception{
        return super.isValidInteger("La opcion ", option);
    }
    
}
