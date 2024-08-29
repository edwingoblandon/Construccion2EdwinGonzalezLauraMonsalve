package app.dao.interfaces;

import app.dto.PartnerDto;

public interface PartnerDao {
    
    public void createUser(PartnerDto partnerDto) throws Exception;
    
    public boolean existsByUserName(PartnerDto partnerDto) throws Exception;
    
    public PartnerDto findByUserName(PartnerDto partnerDto) throws Exception;
}
