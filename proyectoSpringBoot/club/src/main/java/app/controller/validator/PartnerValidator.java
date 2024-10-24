
package app.controller.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class PartnerValidator extends CommonsValidator{
    
    public double validAmount(String amount) throws Exception{
        if(Double.valueOf(amount) <= 0) throw new Exception("El valor a recargar debe ser mayor a $0");
        return super.isValidDouble("El monto del socio", amount);
    }
    
    public long validId(String id) throws Exception{
        return super.isValidLong("El id del socio", id);
    }
}
