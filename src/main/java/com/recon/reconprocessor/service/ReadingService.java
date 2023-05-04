package com.recon.reconprocessor.service;

import com.recon.reconprocessor.model.ReconData;
import com.recon.reconprocessor.model.ReconFile;
import com.recon.reconprocessor.repository.DataRepository;
import com.recon.reconprocessor.repository.ReconFileRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wildfly.common.annotation.NotNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadingService {

  @NotNull
  private final ReconFileRepository reconFileRepository;
  @NotNull
  private final DataRepository dataRepository;

  private ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(50);
    executor.setMaxPoolSize(1000);
    executor.setThreadNamePrefix("threadPoolTaskExecutor");
    executor.initialize();
    return executor;
  }

  public void uploadAndProcessFile(MultipartFile multipart, Integer flag) {
    BufferedReader br;
    List<String> result = new ArrayList<>();
    try {
      String line;
      var is = multipart.getInputStream();
      br = new BufferedReader(new InputStreamReader(is));
      ReconFile file = new ReconFile();
      file.setName(multipart.getName());
      int rows = 0;
      file.setRowsRead(rows);
      file.setFileFlag(flag);
      var reconFile = reconFileRepository.saveAndFlush(file);
      while ((line = br.readLine()) != null) {
        rows++;
        String finalLine = line;
        result.add(finalLine);
        var threadPoolTaskExecutor = getThreadPoolTaskExecutor();
        threadPoolTaskExecutor.execute(() -> {
          saveDataInDb(finalLine, reconFile);
        });
      }
      reconFile.setRowsRead(rows);
      reconFileRepository.saveAndFlush(reconFile);
    } catch (IOException e) {
      log.error("error ", e);
    }
  }

  public void saveDataInDb(String line, ReconFile reconFile) {
    var reconData = new ReconData();
    reconData.setFileDataOne(line);
    reconData.setRecFileId(reconFile.getId());
    dataRepository.saveAndFlush(reconData);
  }

  public List<ReconFile> getAllFile(Integer fileFlag) {
    return reconFileRepository.findAllByFileFlag(fileFlag);
  }
}
