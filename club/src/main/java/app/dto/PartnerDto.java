
package app.dto;

import java.time.LocalDateTime;

public class PartnerDto {
    private long id;
    private UserDto userId;
    private double amaunt;
    private boolean type;
    private LocalDateTime dateOfCreation;
    
    public PartnerDto(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDto getUserId() {
        return userId;
    }

    public void setUserId(UserDto userId) {
        this.userId = userId;
    }

    public double getAmaunt() {
        return amaunt;
    }

    public void setAmaunt(double amaunt) {
        this.amaunt = amaunt;
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
