package com.recon.reconprocessor.controller;

import com.recon.reconprocessor.dto.ChatRequest;
import com.recon.reconprocessor.dto.ChatResponse;
import com.recon.reconprocessor.model.ReconFile;
import com.recon.reconprocessor.service.ReadingService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

  public static void chatGPT(String text) throws Exception {
    String url = "https://api.openai.com/v1/completions";
    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Authorization",
        "Bearer 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");

    JSONObject data = new JSONObject();
    data.put("model", "text-davinci-003");
    data.put("prompt", text);
    data.put("max_tokens", 4000);
    data.put("temperature", 1.0);

    con.setDoOutput(true);
    con.getOutputStream().write(data.toString().getBytes());

    String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
        .reduce((a, b) -> a + b).get();

    System.out.println(
        new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));
  }

  public static void main(String[] args) throws Exception {
    chatGPT("I'm also good bro?");
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
    // call the API
    ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
    if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
      return "No response";
    }
    // return the first response
    return response.getChoices().get(0).getMessage().getContent();
  }
}
