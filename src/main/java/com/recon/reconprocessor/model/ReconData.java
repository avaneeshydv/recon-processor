package com.recon.reconprocessor.model;

import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReconData {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigInteger id;
  @Column
  private BigInteger recFileId;
  @Column
  private String fileDataOne;
  @Column
  private String fileDataTwo;
  @Column
  private BigInteger percent;
}
