package com.recon.reconprocessor.repository;

import java.math.BigInteger;

import com.recon.reconprocessor.model.ColumnDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnDetailsRepository extends JpaRepository<ColumnDetails, BigInteger> {
}
