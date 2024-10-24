package app.controller.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceResponse {
    private long id;
    private String userName;
    private String userType;
    private LocalDateTime dateOfCreation;
    private String status;
    private long itemId;
    private String description;
    private double unitPrice;
    private int quantity;
    private double totalAmount;
}
