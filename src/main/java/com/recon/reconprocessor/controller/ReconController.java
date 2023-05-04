package com.recon.reconprocessor.controller;

import com.recon.reconprocessor.model.ReconFile;
import com.recon.reconprocessor.service.OpenAiService;
import com.recon.reconprocessor.service.ReadingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.wildfly.common.annotation.NotNull;

@RestController
@SpringBootApplication
@RequiredArgsConstructor
public class ReconController {

  @NotNull
  private final ReadingService readingService;
  @NotNull
  private final OpenAiService openAiService;

  @GetMapping("/test")
  ResponseEntity<String> getTest(@RequestParam String prompt) {
    return ResponseEntity.ok(openAiService.chat(prompt));
  }

  @PostMapping("/upload")
  public ResponseEntity<String> getUpload(@RequestParam MultipartFile multipart,
                                          @RequestParam Integer flag) {
    readingService.uploadAndProcessFile(multipart, flag);
    return ResponseEntity.ok("file upload successfully");
  }

  @GetMapping("/getFile")
  public ResponseEntity<List<ReconFile>> getAllFiles(
      @RequestParam Integer flag) {
    return ResponseEntity.ok(readingService.getAllFile(flag));
  }
}
