package com.finx.contractservice.infra.repository.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

@Entity
@Data
@Table(name = "esign_log")
public class ESignLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esign_log_id_seq_gen")
    @SequenceGenerator(
            name = "esign_log_id_seq_gen",
            sequenceName = "esign_log_id_seq",
            allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private long contractId;

    @Column(nullable = false)
    private int httpCode;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode requestPayload;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode responsePayload;

    @Column(nullable = false)
    private String endpoint;
}
