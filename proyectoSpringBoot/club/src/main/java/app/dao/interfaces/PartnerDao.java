package app.dao.interfaces;

import app.dto.PartnerDto;
import app.dto.UserDto;
import java.util.List;

public interface PartnerDao {
    
    public void createPartner(PartnerDto partnerDto) throws Exception;
    
    public boolean existsById(PartnerDto partnerDto) throws Exception;
    
    public PartnerDto findById(PartnerDto partnerDto) throws Exception;
    
    public void updatePartner(PartnerDto partnerDto) throws Exception;
    
    public long countPartnersVip() throws Exception;
    public PartnerDto findByUserId( UserDto userDto ) throws Exception; 
    public List<PartnerDto> findAllPartners() throws Exception;
}
