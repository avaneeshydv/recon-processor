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
public class ReconFile {
  @Id
  @Column
  private BigInteger id;
  @Column
  private String name;
  @Column
  private String type;
  @Column
  private Integer fileFlag;
  @Column
  private Integer rowsRead;
  @Column
  private String reqColumns;

}
