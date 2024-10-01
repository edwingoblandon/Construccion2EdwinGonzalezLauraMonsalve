package app.dao;

import app.dao.interfaces.GuestDao;
import app.dao.repository.GuestRepository;
import app.dto.GuestDto;
import app.dto.PartnerDto;
import app.dto.UserDto;
import app.helpers.Helper;
import app.model.Guest;
import java.util.List;


import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Setter
@Getter
@NoArgsConstructor
@Service
public class GuestDaoImplementation implements GuestDao {
    
    @Autowired
    GuestRepository guestRepository;
    
    @Override
    public boolean existsById(GuestDto guestDto) throws Exception {
        return guestRepository.existsById(guestDto.getId());
    }
    
    @Override
    public void createGuest(GuestDto guestDto) throws Exception {
        Guest guest = Helper.parse(guestDto);
        guestRepository.save(guest);
        guestDto.setId(guest.getId());
    }
    
    @Override
    public GuestDto findById(GuestDto guestDto) throws Exception {
        Optional<Guest> optionalGuest = guestRepository.findById(guestDto.getId());
        
        if(!optionalGuest.isPresent()) throw new Exception("El invitado no se encontro");
        
        Guest guest = optionalGuest.get();
        return Helper.parse(guest);
    }
    
    @Override
    public GuestDto findByUserId(UserDto userDto) throws Exception {
        Guest guest = guestRepository.findByUserId(userDto.getId());
    
        if(guest == null) throw new Exception("No se encontro el usuario del invitado");
        
        return Helper.parse(guest);
       
    }
    
    @Override
    public int countActiveGuestsByPartnerId(GuestDto guestDto) throws Exception {
        return guestRepository.countByPartnerIdAndStatus(Helper.parse(guestDto.getPartnerId()), "Active");
    }

    @Override
    public void updateGuest(GuestDto guestDto) throws Exception {
        Guest guest = Helper.parse(guestDto);
        guestRepository.save(guest);
    }
    
    
    @Override
    public List<GuestDto> findAllGuestsByPartnerId(PartnerDto partnerDto) throws Exception {
        List<Guest> guests = guestRepository.findByPartnerId(partnerDto.getId());

        if (guests.isEmpty()) throw new Exception("No se encontraron invitados asociados a este socio.");

        return guests.stream().map(Helper::parse).collect(Collectors.toList());
    }
    
}

