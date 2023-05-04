package com.recon.reconprocessor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@SpringBootApplication
public class ReconProcessorApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReconProcessorApplication.class, args);
  }

  @Bean
  @Qualifier("openaiRestTemplate")
  public RestTemplate openaiRestTemplate() {
    return new RestTemplate();
  }

}
