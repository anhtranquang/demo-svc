package com.finx.contractservice.infra.repository.dao;

import com.finx.contractservice.core.enumeration.ContractStatus;
import com.finx.contractservice.infra.repository.entity.ContractEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractDao extends JpaRepository<ContractEntity, Long> {

    Optional<ContractEntity> findByIdAndPartyId(Long id, long partyId);

    @Query(
            "SELECT u FROM ContractEntity u WHERE u.contractNo = ?1 AND u.partyId = ?2 ORDER BY u.id DESC LIMIT 1")
    Optional<ContractEntity> findByContractNoAndPartyId(String contractNo, long partyId);

    @Modifying
    @Query(value = """
            UPDATE ContractEntity u
            SET u.status = ?1, u.signedUrl = ?2
            WHERE u.id = ?3
            """)
    int updateStatusAndUrl(ContractStatus status, String url, long contractId);
}
