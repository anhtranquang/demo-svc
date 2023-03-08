package com.finx.contractservice.infra.repository.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.finx.contractservice.core.enumeration.LanguageCode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "contract_draft")
public class ContractDraftEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_draft_id_seq_gen")
    @SequenceGenerator(
            name = "contract_draft_id_seq_gen",
            sequenceName = "contract_draft_id_seq",
            allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageCode languageCode;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private JsonNode data;

    @Type(JsonType.class)
    @Column(columnDefinition = "addition_data")
    private JsonNode additionData;

    @Column(nullable = false)
    private long partyId;

    @Column(name = "template_id", nullable = false)
    private long templateId;

    private String url;
}
