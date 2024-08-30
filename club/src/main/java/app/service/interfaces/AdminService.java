package app.service.interfaces;

import app.dto.PartnerDto;

public interface AdminService {
    public void createPartner(PartnerDto partnerDto) throws Exception;
}
