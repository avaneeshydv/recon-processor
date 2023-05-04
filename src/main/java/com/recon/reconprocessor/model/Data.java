package com.recon.reconprocessor.model;

import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Data {
  @Id
  private BigInteger id;
  private BigInteger recFileId;
  private String data;
  private String data1;
  private BigInteger percent;
}
