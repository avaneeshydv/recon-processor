package com.recon.reconprocessor.service;

import com.recon.reconprocessor.model.Data;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wildfly.common.annotation.NotNull;

@Service
@RequiredArgsConstructor
public class ReadingService {

  @NotNull
  private final ReconFileRepository reconFileRepository;
  @NotNull
  private final DataRepository dataRepository;

  private ThreadPoolTaskExecutor getThreadPoolTaskExecutor(){
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(50);
    executor.setMaxPoolSize(1000);
    executor.setThreadNamePrefix("threadPoolTaskExecutor");
    executor.initialize();
    return executor;
  }

  /**
   * reading service
   * @param multipart
   */
  public void readService(MultipartFile multipart, String flag) {
    BufferedReader br;
    List<String> result = new ArrayList<>();
    try {
      String line;
      InputStream is = multipart.getInputStream();
      br = new BufferedReader(new InputStreamReader(is));
      ReconFile file = new ReconFile();
      file.setName(multipart.getName());
      int rows = 0;
      file.setRowsRead(rows);
      file.setFileFlag(flag);
      var reconFile = reconFileRepository.save(file);
      while ((line = br.readLine()) != null) {
        rows ++;
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
      System.err.println(e.getMessage());
    }
  }

  public void saveDataInDb(String line, ReconFile reconFile) {
    Data data = new Data();
    data.setData(line);
    data.setRecFileId(reconFile.getId());
    dataRepository.saveAndFlush(data);
  }


}
