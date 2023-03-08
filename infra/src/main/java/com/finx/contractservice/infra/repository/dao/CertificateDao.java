package com.finx.contractservice.infra.repository.dao;

import com.finx.contractservice.infra.repository.entity.ContractCertificateEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateDao extends JpaRepository<ContractCertificateEntity, Long> {

    @Query("""
             SELECT u FROM ContractCertificateEntity u 
             WHERE u.partyId = ?1 
                AND (now() BETWEEN u.validFrom AND u.validTo OR u.validFrom = null)
             ORDER BY u.id desc 
             LIMIT 1
                    """)
    Optional<ContractCertificateEntity> findValidCertificateByPartyId(long partyId);

    Optional<ContractCertificateEntity> findByAgreementId(String agreementId);
}
