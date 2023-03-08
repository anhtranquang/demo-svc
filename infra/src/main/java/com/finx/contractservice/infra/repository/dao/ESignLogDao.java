package com.finx.contractservice.infra.repository.dao;

import com.finx.contractservice.infra.repository.entity.ESignLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ESignLogDao extends JpaRepository<ESignLogEntity, Long> {}
