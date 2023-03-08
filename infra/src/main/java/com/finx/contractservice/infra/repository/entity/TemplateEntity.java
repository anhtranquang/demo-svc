package com.finx.contractservice.infra.repository.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.finx.contractservice.core.enumeration.LanguageCode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "template")
public class TemplateEntity extends BaseEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "template_id_seq_gen")
    @SequenceGenerator(
            name = "template_id_seq_gen",
            sequenceName = "template_id_seq",
            allocationSize = 1)
    private Long id;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode metadata;

    private String templateUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageCode languageCode;

    private LocalDate validFrom;
    private LocalDate validTo;
    private String templateName;
}
