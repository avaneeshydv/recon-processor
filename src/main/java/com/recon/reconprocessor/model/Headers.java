package com.recon.reconprocessor.model;

import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Headers {
  @Id
  private BigInteger id;
  private BigInteger fileId;
  private String reqColumns;
  private String matchingColumns;
}
