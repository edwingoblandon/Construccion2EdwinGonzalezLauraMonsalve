package app.dao.interfaces;

import app.dto.PartnerDto;
import app.dto.UserDto;

public interface PartnerDao {
    
    public void createPartner(PartnerDto partnerDto) throws Exception;
    
    public boolean existsByPartnerId(PartnerDto partnerDto) throws Exception;
    
    public PartnerDto findByPartnerId(PartnerDto partnerDto) throws Exception;
    
    public void deletePartner(PartnerDto partnerDto) throws Exception;
    
    public void updatePartner(PartnerDto partnerDto) throws Exception;
    
    public long countPartnersVip() throws Exception;
   public PartnerDto findByUserId( UserDto userDto ) throws Exception;
}
