package app.dao;


import app.dao.interfaces.PartnerDao;
import app.dao.repository.PartnerRepository;
import app.dto.PartnerDto;
import app.dto.UserDto;
import app.helpers.Helper;
import app.model.Partner;

import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@Service
public class PartnerDaoImplementation implements PartnerDao {
    
    @Autowired
    PartnerRepository partnerRepository;
    
    @Override
    public boolean existsById(PartnerDto partnerDto) throws Exception {
        return partnerRepository.existsById(partnerDto.getId());
    }
        

    @Override
    public void createPartner(PartnerDto partnerDto) throws Exception {
        Partner partner = Helper.parse(partnerDto);
        partnerRepository.save(partner);
        partnerDto.setId(partner.getId());
    }
    
    @Override
    public PartnerDto findById(PartnerDto partnerDto) throws Exception {
        Optional<Partner> optionalPartner = partnerRepository.findById(partnerDto.getId());
        
        if(!optionalPartner.isPresent()) throw new Exception("El socio no se encontro");
        
        Partner partner = optionalPartner.get();
        
        return Helper.parse(partner);
    }
    
    @Override
    public void updatePartner(PartnerDto partnerDto){
        //pass
    }
    
    
    @Override
    public long countPartnersVip() throws Exception {
        return partnerRepository.countByType("VIP");
    }
    
    @Override
    public PartnerDto findByUserId(UserDto userDto) throws Exception {
        Partner partner = partnerRepository.findByUserId(userDto.getId());
    
        if(partner == null) throw new Exception("No se encontro un socio asociado al invitado");
        
        return Helper.parse(partner);
       
    }
} 
