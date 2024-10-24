package app.controller.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PartnerResponse {
    private long id;
    private String userName; 
    private String name;
    private double amount;
    private LocalDateTime creationDate;
    private long cellPhone;
    private int invoicesPaid;
}
