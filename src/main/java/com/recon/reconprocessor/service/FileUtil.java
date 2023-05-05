package com.recon.reconprocessor.service;


import io.undertow.util.BadRequestException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@NoArgsConstructor
public class FileUtil {

  @Value("${file.admin.path}")
  private String fileAdminPath;
  @Value("${file.sub.path}")
  private String fileSubPath;
  @Value("${file.relative.path}")
  private String relativePath;

  /**
   * method to clone inputStream.
   *
   * @param inputStream inputStream
   * @return inputStream
   * @throws IOException ioException
   */
  public InputStream cloneInputStream(final InputStream inputStream) throws IOException {
    var outputStream = new ByteArrayOutputStream();
    var buffer = new byte[1024];
    var readLength = 0;
    while (readLength != -1) {
      readLength = inputStream.read(buffer);
      if (readLength == -1) {
        break;
      }
      outputStream.write(buffer, 0, readLength);
    }
    outputStream.flush();
    return new ByteArrayInputStream(outputStream.toByteArray());
  }

  /**
   * method to delete file from local.
   *
   * @param absoluteFilePath absolute file path
   * @return Boolean
   */
  public Boolean deleteFromLocal(String absoluteFilePath) {
    try {
      return Files.deleteIfExists(Path.of(absoluteFilePath));
    } catch (Exception e) {
      log.error("Error occurred while deleting file from local: ", e);
      return false;
    }
  }

  /**
   * method to generate admin file path.
   *
   * @param folderName name of folder
   * @param date       current date
   * @return String
   */
  public String generateAdminFilePath(String folderName, LocalDate date) {
    return getFilePath(folderName, date);
  }

  /**
   * method to generate admin file path.
   *
   * @return String
   */
  public String generateAdminFilePath() {
    return getFilePath(null, null);
  }

  /**
   * method to get file path.
   *
   * @param folderName name of folder
   * @param date       date
   * @return String which denotes path
   */
  private String getFilePath(String folderName, LocalDate date) {
    if (null == date) {
      date = LocalDate.now();
    }
    var optionalFilePath = "";
    if (StringUtils.isNotBlank(folderName)) {
      optionalFilePath = folderName + ReconConstant.FORWARD_SLASH_CHAR;
    }
    var subPath = fileSubPath;
    var filePath = prependAdminPath(date.getYear() + ReconConstant.FORWARD_SLASH_STRING
        + (date.getMonthValue() < 10 ? "0" + date.getMonthValue()
        : String.valueOf(date.getMonthValue()))
        + ReconConstant.FORWARD_SLASH_STRING
        + (date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth()
        : String.valueOf(date.getDayOfMonth()))
        + ReconConstant.FORWARD_SLASH_STRING + subPath + ReconConstant.FORWARD_SLASH_CHAR
        + optionalFilePath);
    var directoryCreated = createFolderAndSetPermission(filePath);
    log.info("Directory : " + filePath + " created for first time: " + directoryCreated);

    return filePath;
  }

  /**
   * function to create folder and set permission.
   *
   * @param path path of file
   * @return Boolean
   */
  private boolean createFolderAndSetPermission(String path) {

    String basePath = fileAdminPath;
    String pathToBeChecked = getRelativePathFromAdminPath(path);
    var fileHandle = new File(basePath);
    if (!fileHandle.exists() || !fileHandle.isDirectory()) {
      log.info("File Admin Base path does not exists: " + basePath);
      return false;
    }
    String[] pathArray = pathToBeChecked.split(ReconConstant.FORWARD_SLASH_STRING);
    var tempPath = new StringBuilder();
    for (var i = 0; i < pathArray.length; i++) {
      tempPath.append(pathArray[i] + ReconConstant.FORWARD_SLASH_CHAR);
      fileHandle = new File(basePath + tempPath);
      if (!fileHandle.exists() || !fileHandle.isDirectory()) {
        var f = new File(basePath + tempPath);
        Boolean directoryCreated = f.mkdir();
        if (Boolean.TRUE.equals(directoryCreated)) {
          log.info("Directory created: " + basePath + tempPath);
        } else {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * method to get relative path of a file from admin path.
   *
   * @param filePath path of file
   * @return String
   */
  public String getRelativePathFromAdminPath(String filePath) {
    if (relativePath == null) {
      return filePath;
    }
    if (filePath.startsWith(relativePath)) {
      filePath = filePath.substring(relativePath.length());
    }
    return filePath;
  }

  /**
   * function to prepend admin path to a filename.
   *
   * @param file filename
   * @return String
   */
  public String prependAdminPath(String file) {
    String baseAdminPath = this.relativePath;
    var fileName = "";
    if (baseAdminPath != null) {
      fileName = baseAdminPath;
    }
    fileName = fileName + file;
    return fileName;
  }

  /**
   * function to get path from where to read in sftp.
   *
   * @param basePath base path
   * @return String which contains the path from where the file is to be read.
   */
  public String dateWiseSftpReadPath(String basePath) {
    return basePath + LocalDate.now().getYear() + ReconConstant.FORWARD_SLASH_STRING
        + (LocalDate.now().getMonthValue() < 10
        ? "0" + LocalDate.now().getMonthValue()
        : String.valueOf(LocalDate.now().getMonthValue()))
        + ReconConstant.FORWARD_SLASH_STRING
        + (LocalDate.now().getDayOfMonth() < 10
        ? "0" + LocalDate.now().getDayOfMonth()
        : String.valueOf(LocalDate.now().getDayOfMonth()))
        + ReconConstant.FORWARD_SLASH_STRING;
  }

  /**
   * method to get extension of fileName.
   *
   * @param fileName name of file
   * @return String
   */
  public String getExtension(String fileName) {
    String[] splitFile = StringUtils.split(fileName, SettlementUtil.getSeparator("."));
    if (splitFile.length != 2) {
      try {
        throw new BadRequestException("Invalid filename provided");
      } catch (BadRequestException e) {
        throw new RuntimeException(e);
      }
    }
    return splitFile[splitFile.length - 1];
  }

  /**
   * function to get xls and xlsx file content from a sheet.
   *
   * @param sheet       sheet
   * @param datePattern datePattern
   * @return String.
   */
  public String getXlsXlsxFileContent(Sheet sheet, String datePattern) {
    Integer rowCounter;
    var fileDataBuilder = new StringBuilder(100);
    var lineData = new StringBuilder();
    Integer rowCount = sheet.getLastRowNum();
    Integer emptyRowCount = 0;
    Integer prevEmptyRowNum = 0;
    var plainRowData = new StringBuilder();
    log.info("starting reading rowWise.");
    for (var i = 0; i <= rowCount; i++) {
      lineData.setLength(0);
      plainRowData.setLength(0);
      rowCounter = 0;
      var row = sheet.getRow(i);
      plainRowData.append(getPlainRowData(lineData, plainRowData, rowCounter, row, datePattern));
      if (null == row || plainRowData.length() < ReconConstant.MINIMUM_LENGTH_OF_ROW_DATA_IN_FILE) {
        if (prevEmptyRowNum.equals(i - 1)) {
          emptyRowCount++;
        } else {
          emptyRowCount = 1;
        }
        prevEmptyRowNum = i;
      }
      if (ReconConstant.MAXIMUM_EMPTY_ROWS_IN_FILE.equals(emptyRowCount)) {
        fileDataBuilder.append("Stopped reading file further due to consecutive "
            + ReconConstant.MAXIMUM_EMPTY_ROWS_IN_FILE + " rows with less than "
            + ReconConstant.MINIMUM_LENGTH_OF_ROW_DATA_IN_FILE + " characters\n");
        break;
      }
      if (lineData.length() > 0) {
        fileDataBuilder =
            fileDataBuilder.append(lineData.substring(0, lineData.length() - 1) + "\n");
      } else {
        fileDataBuilder = fileDataBuilder.append("\n");
      }
    }
    return fileDataBuilder.toString();
  }

  /**
   * function to get row data.
   *
   * @param lineData     lineData
   * @param plainRowData plainRowData
   * @param rowCounter   rowCounter
   * @param row          row
   * @param datePattern  datePattern
   * @return StringBuilder
   */
  private StringBuilder getPlainRowData(StringBuilder lineData, StringBuilder plainRowData,
                                        Integer rowCounter, Row row, String datePattern) {
    Cell cell;
    String value;
    if (row == null) {
      lineData.append("\t");
    } else {
      while (rowCounter < row.getLastCellNum()) {
        cell = row.getCell(rowCounter++);
        if (cell == null) {
          value = "";
        } else if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
          value = TimeUtil.formatDate(
              cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
              TimeUtil.getDateTimeFormatter(datePattern));
        } else {
          var formatter = new DataFormatter(Locale.US);
          value = formatter.formatCellValue(cell);
        }
        value = value.substring(value.indexOf("'") + 1);
        lineData.append(value).append("\t");
        plainRowData.append(value.trim());
      }
    }
    return plainRowData;
  }


  /**
   * method to keep a file into a dir.
   *
   * @param filesData     data
   * @param localFilePath path
   * @throws IOException exception
   */
  public void createFileFromMultipartFile(MultipartFile filesData, String localFilePath)
      throws IOException {
    var filePath = filesData.getOriginalFilename();
    var inputStream = filesData.getInputStream();
    var file = new File(localFilePath + filePath);
    if (!FileUtils.directoryContains(new File(localFilePath), file)) {
      Boolean isCreated = file.createNewFile();
      log.info("File Created on local {} {}", localFilePath, isCreated);
    }
    FileUtils.copyInputStreamToFile(inputStream, file);
  }

  /**
   * function to read specific files.
   *
   * @param inputStream inputStream
   * @return List of String
   */
  public static List<String> readSpecificFiles(InputStream inputStream) {
    String lineData;
    var fileDataBuilder = new StringBuilder();
    try (Reader inputStreamReader = new InputStreamReader(inputStream);
         var reader = new BufferedReader(inputStreamReader)) {
      while (true) {
        lineData = reader.readLine();
        if (lineData == null) {
          break;
        }
        fileDataBuilder.append(lineData.trim()).append("\n");
      }
    } catch (IOException ex) {
      log.error("IOException occurred when reading file from input stream ", ex);
      return new ArrayList<>();
    }
    var fileDataString = fileDataBuilder.toString();
    var fileDataStrings = fileDataString.split("\n");
    return new ArrayList<>(Arrays.asList(fileDataStrings));
  }
}
