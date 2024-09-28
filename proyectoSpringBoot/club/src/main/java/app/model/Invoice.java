package app.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="invoice")
public class Invoice {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;
    @JoinColumn(name="userid")
    @ManyToOne
    private User userId;
    @JoinColumn(name="partnerid")
    @ManyToOne
    private Partner partnerId;
    @Column(name="creationdate")
    private LocalDateTime creationDate;
    @Column(name="totalamount")
    private double totalAmount;
    @Column(name="status")
    private String status; 
}
