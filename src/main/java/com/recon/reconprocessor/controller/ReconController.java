package com.recon.reconprocessor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReconController {

  @GetMapping("/test")
  ResponseEntity<String> getTest() {
    return ResponseEntity.ok("success");
  }
}
