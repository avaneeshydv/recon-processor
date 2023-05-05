package com.recon.reconprocessor.repository;

import com.recon.reconprocessor.model.ReconOutput;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconOutputRepository extends JpaRepository<ReconOutput, BigInteger> {
}
