package com.recon.reconprocessor.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication

public class ReconController {

  @GetMapping("/test")
  ResponseEntity<String> getTest() {
    return ResponseEntity.ok("success");
  }

  public static void chatGPT(String text) throws Exception {
    String url = "https://api.openai.com/v1/completions";
    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Authorization", "Bearer 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");

    JSONObject data = new JSONObject();
    data.put("model", "text-davinci-003");
    data.put("prompt", text);
    data.put("max_tokens", 4000);
    data.put("temperature", 1.0);

    con.setDoOutput(true);
    con.getOutputStream().write(data.toString().getBytes());

    String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
        .reduce((a, b) -> a + b).get();

    System.out.println(new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));
  }

  public static void main(String[] args) throws Exception {
    chatGPT("I'm also good bro?");
  }
}
