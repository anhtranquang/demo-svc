package com.finx.contractservice.core.domain;

import com.finx.contractservice.core.enumeration.LanguageCode;
import java.time.LocalDate;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractTemplate {
    private Long id;
    private Map<String, String> metadata;
    private String templateUrl;
    private LanguageCode languageCode;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String templateName;
}
