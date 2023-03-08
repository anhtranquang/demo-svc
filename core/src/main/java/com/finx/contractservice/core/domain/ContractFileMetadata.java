package com.finx.contractservice.core.domain;

import com.finx.contractservice.core.enumeration.LanguageCode;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractFileMetadata {
    private Long draftId;
    private long templateId;
    private String templateName;
    private Map<String, String> data;
    private Map<String, String> additionData;
    @Builder.Default private LanguageCode languageCode = LanguageCode.VI;
    private Long partyId;
    private String path;
    private String url;
}
