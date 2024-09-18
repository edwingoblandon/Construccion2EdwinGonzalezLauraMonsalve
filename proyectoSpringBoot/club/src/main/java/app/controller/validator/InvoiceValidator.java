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
    
    public void validId(String id) throws Exception{
        super.isValidLong("El id de la factura ", id);
    }
    
    public void validTotalAmount(String value) throws Exception{
        super.isValidDouble("El monto total ", value);
    }
}
