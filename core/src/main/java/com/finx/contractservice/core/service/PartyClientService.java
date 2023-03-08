package com.finx.contractservice.core.service;

import com.finx.contractservice.core.domain.ContractParty;

public interface PartyClientService {

    /**
     * Get current partyDetail
     *
     * @param partyId
     * @return ContractParty {@link ContractParty}
     */
    ContractParty getDetailByPartyId(long partyId);
}
