package com.finx.contractservice.infra.event.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ContractSignedEvent extends ApplicationEvent {

    private String agreementId;

    public ContractSignedEvent(Object source, String agreementId) {
        super(source);
        this.agreementId = agreementId;
    }
}
