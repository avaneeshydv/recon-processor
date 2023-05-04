package com.recon.reconprocessor.repository;

import com.recon.reconprocessor.model.Headers;
import java.math.BigInteger;
import javax.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeaderRepository extends JpaRepository<Headers, BigInteger> {
}
