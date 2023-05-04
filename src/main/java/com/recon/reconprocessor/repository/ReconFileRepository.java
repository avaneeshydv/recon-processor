package com.recon.reconprocessor.repository;

import com.recon.reconprocessor.model.ReconFile;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconFileRepository extends JpaRepository<ReconFile, BigInteger> {

  List<ReconFile> findAllByFileFlag(Integer fileFlag);
}
