package app.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreationInvoiceRequest {
    private String id;
    private String quantity;
    private String paymentOption;
}
