package com.recon.reconprocessor.service;

import com.google.gson.Gson;
import com.recon.reconprocessor.dto.ChatRequest;
import com.recon.reconprocessor.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiService {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${openai.model}")
  private String model;

  @Value("${openai.api.url}")
  private String apiUrl;

  @Value("${openai.api.key}")
  private String key;

  public String chat(@RequestParam String prompt) {
    // create a request
    ChatRequest request = new ChatRequest(model, prompt);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(key);
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
