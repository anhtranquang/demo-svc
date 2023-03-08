package com.finx.contractservice.infra.repository.dao;

import com.finx.contractservice.infra.repository.entity.TemplateEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateDao extends JpaRepository<TemplateEntity, Long> {

    @Query(
            "SELECT u FROM TemplateEntity u WHERE u.templateName = :templateName ORDER BY u.id desc LIMIT 1")
    Optional<TemplateEntity> findByTemplateByTemplateName(@Param("templateName") String templateName);
}
