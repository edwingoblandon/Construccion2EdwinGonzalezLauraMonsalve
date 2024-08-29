package app.model;
import java.time.LocalDateTime;

public class Partner {
    private long id;
    private User userId;
    private double amount;
    private boolean type;
    private LocalDateTime dateOfCreation;
    
    public Partner(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmaunt(double amount) {
        this.amount = amount;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
    
    
}
