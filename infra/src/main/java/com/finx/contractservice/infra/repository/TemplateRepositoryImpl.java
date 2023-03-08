package com.finx.contractservice.infra.repository;

import com.finx.contractservice.core.domain.ContractTemplate;
import com.finx.contractservice.core.repository.TemplateRepository;
import com.finx.contractservice.infra.repository.dao.TemplateDao;
import com.finx.contractservice.infra.repository.entity.TemplateEntity;
import com.finx.contractservice.infra.repository.mapper.ContractEntityMapper;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TemplateRepositoryImpl implements TemplateRepository {
    private final TemplateDao templateDao;
    private final ContractEntityMapper contractEntityMapper;

    public TemplateRepositoryImpl(
            TemplateDao templateDao, ContractEntityMapper contractEntityMapper) {
        this.templateDao = templateDao;
        this.contractEntityMapper = contractEntityMapper;
    }

    @Override
    public Optional<ContractTemplate> findLatestTemplateByName(String templateName) {
        Optional<TemplateEntity> entity = templateDao.findByTemplateByTemplateName(templateName);
        return entity.map(contractEntityMapper::toTemplateModel);
    }

    @Override
    public Optional<ContractTemplate> findTemplateById(long id) {
        Optional<TemplateEntity> entity = templateDao.findById(id);
        return entity.map(contractEntityMapper::toTemplateModel);
    }
}
