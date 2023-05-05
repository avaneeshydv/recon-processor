package com.recon.reconprocessor.service;

import com.recon.reconprocessor.model.ReconData;
import com.recon.reconprocessor.model.ReconFile;
import com.recon.reconprocessor.repository.DataRepository;
import com.recon.reconprocessor.repository.ReconFileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
  @NotNull
  private final XlsxService xlsxService;
  @NotNull
  private final OpenAiService openAiService;

  private ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(50);
    executor.setMaxPoolSize(1000);
    executor.setThreadNamePrefix("threadPoolTaskExecutor");
    executor.initialize();
    return executor;
  }

  public void uploadAndProcessFile(MultipartFile multipart, Integer flag) {
    try {
      ReconFile file = new ReconFile();
      file.setName(multipart.getOriginalFilename());
      file.setFileFlag(flag);
      var dataList = xlsxService.readFileData(multipart.getInputStream(), 0);
      // ask OPEN AI to get type of dock
      var type = getTypeOfDocument(dataList.get(0));
      file.setType(type);
      // ask Open API to give mandatory column to recon
      var columnsRequiredMetaData =
          StringUtils.substringBetween(type, "  <option value=\"", "</option>");
      var columnsRequired = columnsRequiredMetaData.split("\">")[0];
      var mandatoryColumns = getMandatoryColumns(columnsRequired);
      file.setReqColumns(mandatoryColumns);
      file.setRowsRead(dataList.size());
      var data = reconFileRepository.saveAndFlush(file);
      dataList.remove(0);
      var threadPool = getThreadPoolTaskExecutor();
      dataList.forEach(dt -> {
        threadPool.execute(() -> {
          saveDataInDb(dt, data);
        });
      });
    } catch (Exception e) {
      log.error("error ", e);
    }
  }

  private String getTypeOfDocument(String headers) {
    var question = ReconConstant.FILE_TYPE_QUESTION.replace("{dataList}",
        headers);
    question = question.replace("\t", ",");
    var answer = openAiService.chat(question);
    while (Boolean.FALSE.equals(StringUtils.contains(answer, "<"))) {
      answer = openAiService.chat(question);
    }
    return StringUtils.replace(StringUtils.substringBetween(answer, "<select>", "</select>"), "\n",
        "");
  }

  private String getMandatoryColumns(String fileType) {
    var question = ReconConstant.MANDATORY_COLUMNS.replace("{fileType}",
        fileType);
    question = question.replace("\t", ",");
    var answer = openAiService.chat(question);
    while (Boolean.FALSE.equals(StringUtils.contains(answer, "<"))) {
      answer = openAiService.chat(question);
    }
    return StringUtils.replace(StringUtils.substringBetween(answer, "<table>", "</table>"), "\n",
        "");
  }

  public void saveDataInDb(String line, ReconFile reconFile) {
    try {
      var reconData = new ReconData();
      reconData.setRecFileId(reconFile.getId());
      reconData.setFileData(line.replaceAll("\t", ","));
      dataRepository.saveAndFlush(reconData);
    } catch (Exception e) {
      log.error("Error ", e);
    }
  }

  public List<ReconFile> getAllFile(Integer fileFlag) {
    return reconFileRepository.findAllByFileFlag(fileFlag);
  }
}
