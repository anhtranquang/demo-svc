package com.finx.contractservice.infra.service;

import com.finx.contractservice.core.domain.ContractParty;
import com.finx.contractservice.core.service.PartyClientService;
import org.springframework.stereotype.Service;

@Service
public class PartyClientServiceImpl implements PartyClientService {

    public static final String FILE_PERSONAL_BACKSIDE_DOC = "can-cuoc-cong-dan-cmnd.jpg";

    private final CachingPartyService cachingPartyService;

    public PartyClientServiceImpl(CachingPartyService cachingPartyService) {
        this.cachingPartyService = cachingPartyService;
    }

    @Override
    public ContractParty getDetailByPartyId(long partyId) {
        // TODO mock data. Get real data from partyService
        ContractParty contractParty =
                ContractParty.builder()
                        .partyId(partyId)
                        .nid("0123456789")
                        .citizenId("079090008523")
                        .email("quan.nguyen@galaxyfinx.com")
                        .fullName("Võ Văn Huy")
                        .phoneNumber("0357027911")
                        .stateOrProvince("Ho Chi Minh City")
                        .country("VN")
                        .location("Xa Phuoc Kien, Huyen Nha Be")
                        .backSideOfIDDocument("static/" + FILE_PERSONAL_BACKSIDE_DOC)
                        .frontSideOfIDDocument("static/" + FILE_PERSONAL_BACKSIDE_DOC)
                        .build();
        return cachingPartyService.loadParty(contractParty);
    }
}
