package com.recon.reconprocessor.controller;

import com.google.gson.Gson;
import com.recon.reconprocessor.dto.ChatRequest;
import com.recon.reconprocessor.dto.ChatResponse;
import com.recon.reconprocessor.model.ReconFile;
import com.recon.reconprocessor.service.ReadingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.wildfly.common.annotation.NotNull;

@RestController
@SpringBootApplication
@RequiredArgsConstructor
public class ReconController {

  @NotNull
  private final ReadingService readingService;

  @Qualifier("openaiRestTemplate")
  @Autowired
  private RestTemplate restTemplate;

  @Value("${openai.model}")
  private String model;

  @Value("${openai.api.url}")
  private String apiUrl;

  @GetMapping("/test")
  ResponseEntity<String> getTest() {
    return ResponseEntity.ok("success");
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

  @GetMapping("/chat")
  public String chat(@RequestParam String prompt) {
    // create a request
    ChatRequest request = new ChatRequest(model, prompt);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    var requestEntity =
        new HttpEntity<>(new Gson().toJson(request), headers);
    // call the API
    var responseString =
        restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
    var response = new Gson().fromJson(responseString.getBody(), ChatResponse.class);
    if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
      return "No response";
    }
    // return the first response
    return response.getChoices().get(0).getMessage().getContent();
  }
}
