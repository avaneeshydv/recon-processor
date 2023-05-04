package com.recon.reconprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
  private String role;
  private String content;
}
