package com.recon.reconprocessor.model;

import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReconFile {
  @Id
  private BigInteger id;
  private String name;
  private String type;
  private String fileFlag;
  private Integer rowsRead;
  private String reqColumns;

}
