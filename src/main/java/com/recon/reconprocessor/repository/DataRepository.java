package com.recon.reconprocessor.repository;

import com.recon.reconprocessor.model.Data;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends JpaRepository<Data, BigInteger> {
}
