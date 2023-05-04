package com.recon.reconprocessor.repository;

import com.recon.reconprocessor.model.ColumnMapping;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnMappingRepository extends JpaRepository<ColumnMapping, BigInteger> {
}
