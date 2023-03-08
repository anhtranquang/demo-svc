package com.finx.contractservice.infra.repository;

import com.finx.contractservice.core.domain.ContractFileMetadata;
import com.finx.contractservice.core.repository.ContractFileRepository;
import com.finx.contractservice.infra.repository.dao.ContractDraftDao;
import com.finx.contractservice.infra.repository.dao.TemplateDao;
import com.finx.contractservice.infra.repository.entity.ContractDraftEntity;
import com.finx.contractservice.infra.repository.entity.TemplateEntity;
import com.finx.contractservice.infra.repository.mapper.ContractDraftEntityMapper;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ContractFileRepositoryImpl implements ContractFileRepository {

    private final ContractDraftDao contractDraftDao;
    private final TemplateDao templateDao;
    private final ContractDraftEntityMapper contractDraftEntityMapper;

    public ContractFileRepositoryImpl(
            ContractDraftDao contractDraftDao,
            TemplateDao templateDao,
            ContractDraftEntityMapper contractDraftEntityMapper) {
        this.contractDraftDao = contractDraftDao;
        this.templateDao = templateDao;
        this.contractDraftEntityMapper = contractDraftEntityMapper;
    }

    @Override
    public Optional<ContractFileMetadata> findContractFile(Long draftId) {
        Optional<ContractDraftEntity> contractDraftEntity = contractDraftDao.findById(draftId);
        return contractDraftEntity.map(contractDraftEntityMapper::toModel);
    }

    @Override
    public ContractFileMetadata createDraft(ContractFileMetadata contractDetail) {
        Optional<TemplateEntity> templateEntity =
                templateDao.findByTemplateByTemplateName(contractDetail.getTemplateName());

        if (templateEntity.isEmpty()) {
            throw new NullPointerException("Temple is null.");
        }

        ContractDraftEntity entity = contractDraftEntityMapper.toEntity(contractDetail);
        entity.setTemplateId(templateEntity.get().getId());
        ContractDraftEntity savedEntity = contractDraftDao.save(entity);
        contractDetail.setDraftId(savedEntity.getId());
        return contractDetail;
    }
}
