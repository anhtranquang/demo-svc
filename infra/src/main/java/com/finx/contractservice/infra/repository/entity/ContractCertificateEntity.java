package com.finx.contractservice.infra.repository.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "contract_certificate")
public class ContractCertificateEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_certificate_id_seq_gen")
    @SequenceGenerator(
            name = "contract_certificate_id_seq_gen",
            sequenceName = "contract_certificate_id_seq",
            allocationSize = 1)
    private Long id;

    private String certificate;

    private String agreementId;

    @Column(name = "certificate_serial_number")
    private String certificateSerialNumber;

    private String certificateDn;

    @Column(nullable = false)
    private String billCode;

    private ZonedDateTime validFrom;

    private ZonedDateTime validTo;

    @Column(name = "party_id", nullable = false)
    private long partyId;
}
