package app.controller.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class GuestValidator extends CommonsValidator{
    public long validId(String id) throws Exception{
        return super.isValidLong("El id del invitado ", id);
    }
}
