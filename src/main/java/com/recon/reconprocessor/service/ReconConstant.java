package com.recon.reconprocessor.service;

public class ReconConstant {

  private ReconConstant() {
    // private constructor
  }

  public static final Integer MINIMUM_LENGTH_OF_ROW_DATA_IN_FILE = 10;
  public static final Integer MAXIMUM_EMPTY_ROWS_IN_FILE = 10;
  public static final char FORWARD_SLASH_CHAR = '/';
  public static final String FORWARD_SLASH_STRING = "/";

  public static final String FILE_TYPE_QUESTION =
      "which document can have columns as {dataList}? Provide options in html select tag?";

  public static final String MANDATORY_COLUMNS =
      "which are the mandatory columns required for reconciliation for {fileType}? Provide options in html table tag?";

}

