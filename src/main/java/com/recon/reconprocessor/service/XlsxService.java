package com.recon.reconprocessor.service;


import com.recon.reconprocessor.service.FileHandlerService;
import com.recon.reconprocessor.service.FileUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.wildfly.common.annotation.NotNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class XlsxService implements FileHandlerService {

  @NotNull
  private final FileUtil fileUtil;


  @Override
  public List<String> readFileData(InputStream inputStream, int sheetNum) {
    String fileDataString;
    try (var workBook = new XSSFWorkbook(inputStream)) {
      var sheet = workBook.getSheetAt(sheetNum);
      fileDataString = fileUtil.getXlsXlsxFileContent(sheet, null);
    } catch (Exception ex) {
      log.error("IOException occurred when reading file from input stream: {}", ex.getMessage());
      return new ArrayList<>();
    }
    var fileDataStrings = fileDataString.split("\n");
    return new LinkedList<>(Arrays.asList(fileDataStrings));
  }
}
