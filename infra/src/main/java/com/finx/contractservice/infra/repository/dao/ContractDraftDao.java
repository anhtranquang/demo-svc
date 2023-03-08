package com.finx.contractservice.infra.repository.dao;

import com.finx.contractservice.infra.repository.entity.ContractDraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractDraftDao extends JpaRepository<ContractDraftEntity, Long> {}
