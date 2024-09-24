package app.dao;

import app.dao.interfaces.GuestDao;
import app.dao.repository.GuestRepository;
import app.dto.GuestDto;
import app.dto.PartnerDto;
import app.helpers.Helper;
import app.model.Guest;


import java.util.Optional;
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
    public int countActiveGuestsByPartnerId(GuestDto guestDto) throws Exception {
        return guestRepository.countByPartnerIdAndStatus(Helper.parse(guestDto.getPartnerId()), "active");
    }

    @Override
    public void updateGuest(GuestDto guestDto) throws Exception {
        Guest guest = Helper.parse(guestDto);
        guestRepository.save(guest);
    }
}

