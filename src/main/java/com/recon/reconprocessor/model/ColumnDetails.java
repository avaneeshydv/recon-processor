package com.recon.reconprocessor.model;

import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ColumnDetails {
  @Id
  @Column
  private BigInteger id;
  @Column
  private BigInteger fileId;
  @Column
  private String reqColumns;
  @Column
  private String matchingColumns;
}
