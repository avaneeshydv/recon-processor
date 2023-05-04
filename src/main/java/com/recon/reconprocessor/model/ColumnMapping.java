package com.recon.reconprocessor.model;

import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ColumnMapping {
  @Id
  private BigInteger id;

}
