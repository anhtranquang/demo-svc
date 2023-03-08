package com.finx.contractservice.infra.repository.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.finx.contractservice.core.enumeration.ContractStatus;
import com.finx.contractservice.core.enumeration.LanguageCode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "contract")
public class ContractEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_id_seq_gen")
    @SequenceGenerator(
            name = "contract_id_seq_gen",
            sequenceName = "contract_id_seq",
            allocationSize = 1)
    private Long id;

    private String contractNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageCode languageCode;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private JsonNode data;

    @Column(nullable = false)
    private long partyId;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private JsonNode partyDetail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    @ManyToOne
    @JoinColumn(columnDefinition = "certificate_id")
    private ContractCertificateEntity certificate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private TemplateEntity template;

    private String signedUrl;
    private String billCode;
    private String error;
}
