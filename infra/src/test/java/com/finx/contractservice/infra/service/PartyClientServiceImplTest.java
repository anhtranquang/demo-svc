package com.finx.contractservice.infra.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.finx.contractservice.core.domain.ContractParty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PartyClientServiceImplTest {

    @Mock private CachingPartyService cachingPartyService;

    @InjectMocks private PartyClientServiceImpl partyClientService;

    @Test
    void testGetDetailByPartyIdSuccessfully() {
        // When
        String nid = "0123456789";
        when(cachingPartyService.loadParty(any())).thenReturn(ContractParty.builder().nid(nid).build());
        ContractParty party = partyClientService.getDetailByPartyId(1);
        // Then
        assertThat(party.getNid(), is(nid));
    }
}
