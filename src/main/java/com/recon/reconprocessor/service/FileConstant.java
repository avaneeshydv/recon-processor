package com.recon.reconprocessor.service;

import java.util.List;

public class FileConstant {

  private FileConstant() {
    // private constructor
  }

  public static final int FILE_MAX_COLUMN_COUNT = 10;
  public static final Integer MINIMUM_LENGTH_OF_ROW_DATA_IN_FILE = 10;
  public static final Integer MAXIMUM_EMPTY_ROWS_IN_FILE = 10;
  public static final Integer QUEUE_SIZE = 50000;
  public static final Integer DSV_FILE_WRITE_OPERATION = 1;
  public static final Integer DSV_FILE_APPEND_OPERATION = 2;
  public static final Integer WRITER_THREAD_POOL_SIZE = 2;
  public static final String SUCCESS = "Success";

  public static final String UNDER_SCORE = "_";
  public static final String DOT = ".";
  public static final String SWIFT = "_SWIFT";
  public static final String DIFFERENCE_FILE_NAME = "_diff_file_";
  public static final String DOWNLOAD = "download_";
  public static final char FORWARD_SLASH_CHAR = '/';
  public static final String FORWARD_SLASH_STRING = "/";
  public static final String XLS = "xls";

  // Constants for GenericClientSettlementDifferenceFileDto query
  public static final String SETTLEMENT_ID = "settlementId";
  public static final String SETTLEMENT_DATE_STRING = "settlementDateString";
  public static final String AGENT_ID = "agentId";
  public static final String COUNT_DIFF = "countDiff";
  public static final String AMOUNT_DIFF = "amountDiff";
  public static final String AGGREGATED_SETTLEMENT_NET_AMOUNT = "aggregatedSettlementNetAmount";
  public static final String SETTLEMENT_DETAILS_NET_AMOUNT = "settlementDetailsNetAmount";
  public static final String ADJUSTMENT_NET_AMOUNT = "adjustmentNetAmount";
  public static final String ADJUSTMENT_AMOUNT = "adjustmentAmount";
  public static final String DMS_NET_AMOUNT = "dmsNetAmount";
  public static final String GS_NET_AMOUNT = "gsNetAmount";
  public static final String PATH_OF_YML = "summary/";
  public static final String SETTLEMENT_DASHBOARD_FILE = "SETTLEMENT_DATA_";
  public static final List<String> GENERIC_DIFF_FILE_HEADER =
      List.of("Settlement ID", "Aggregated Settlement Net Amount", "Settlement Details Net Amount",
          "Aggregated Settlement Adjustment Amount", "Agent Id", "Amount Difference",
          "Transaction Count Difference");
  public static final List<String> PAYMENT_DIFF_FILE_HEADER = List.of("Settlement Date String",
      "DMS Net Amount", "Gst Details Net Amount", "DMS Adjustment Amount", "Merchant Id",
      "Amount Difference", "Transaction Count Difference");
  // amlock transaction constant
  public static final String ALLOWED_EXTENSION = "xls|xlsx|csv";
  public static final String AMLOCK_TXN = "amlockTransaction";
  public static final String PROCESSED_SUFFIX = "_processed";
  public static final String FILE_NAME_REGEX = "^[\\w\\-. ]+$";

  public static final String MERCHANT_ID = "merchantId";
  public static final String PRE_APPROVED_LIMIT = "preApprovedLimit";
  public static final String PRE_APPROVED = "preApproved";

  public enum UtrFileColumnKeywords {
    MIN_TID("minTid"), MAX_TID("maxTid"), UTR_NUMBER("utr"), ROW_NUMBER("rowNumber"),
    FOREX_AMOUNT("forexAmount"), REQUEST_ID("requestId"), REQUESTED_ACTION("requestedAction"),
    INVOICE_DATE("invoiceDate"), INVOICE_NUMBER("invoiceNumber"), REMARKS("remarks"),
    PAYU_ID("payuId"), AMOUNT("amount");

    public final String value;

    UtrFileColumnKeywords(String value) {
      this.value = value;
    }
  }

}

