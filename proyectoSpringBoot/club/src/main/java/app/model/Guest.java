package app.model;

public class Guest {
    private long id;
    private User userId;
    private Partner partnerId;
    private String status;
    
    public Guest(){}

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

    public Partner getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Partner partnerId) {
        this.partnerId = partnerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
