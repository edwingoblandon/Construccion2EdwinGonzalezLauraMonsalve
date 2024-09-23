
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
    
    public double validAmount(String type, String amount) throws Exception{
        double validAmount = super.isValidDouble("El monto del socio", amount);
        if (validAmount < 0 || validAmount > 5000000) throw new Exception("El monto debe ser mayor o igual a 0 y menor a 5000000");
        
        return validAmount;
    }
}
