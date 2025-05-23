package com.fx_currency_exchange.backend.infrastructure.persistence.jpa.repository;

import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ConversionTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface JpaConversionTransactionRepository extends JpaRepository<ConversionTransactionEntity, UUID>, JpaSpecificationExecutor<ConversionTransactionEntity> {
}