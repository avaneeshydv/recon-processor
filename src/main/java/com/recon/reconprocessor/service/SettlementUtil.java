package com.recon.reconprocessor.service;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class SettlementUtil {

  private SettlementUtil() {
    // private constructor
  }

  /**
   * it compares 2 int (can be null) and give result true if both are equal.
   *
   * @param value1 can be null
   * @param value2 can be nul
   * @return true of false
   */
  public static Boolean compareNullableInteger(Integer value1, Integer value2) {
    if (value1 == null && value2 == null) {
      return Boolean.TRUE;
    } else if (value1 == null || value2 == null) {
      return Boolean.FALSE;
    } else {
      return value1.equals(value2);
    }
  }

  /**
   * function to check if a string in empty or null string.
   *
   * @param value value of string
   * @return boolean result.
   */
  public static boolean isEmptyOrNullString(String value) {
    return StringUtils.isEmpty(value) || "null".equalsIgnoreCase(value);
  }

  /**
   * function to check if a list is empty or null.
   *
   * @param map map
   * @return boolean result.
   */
  public static boolean isEmptyOrNull(Map<Integer, String> map) {
    if (map == null) {
      return true;
    }
    return (map.isEmpty());
  }

  /**
   * function to get separator.
   *
   * @param separator separator
   * @return String
   */
  public static String getSeparator(String separator) {
    var stringBuilder = new StringBuilder();
    stringBuilder.append(separator);
    for (ControlCharacterEnum ch : ControlCharacterEnum.values()) {
      if (ch.value.equals(separator)) {
        stringBuilder = new StringBuilder("\\");
        stringBuilder.append(separator);
        break;
      }
    }
    return stringBuilder.toString();
  }

  /**
   * method to return string in quotes.
   *
   * @param s string
   * @return String enclosed in quotes
   */
  public static String getStringInQuotes(String s) {
    return "'" + s + "'";
  }

  /**
   * check if a give bit values in string is 1.
   *
   * @param settlementBlockingCode code
   * @param position               position
   * @return true or false
   */
  public static boolean isBitSet(String settlementBlockingCode, int position) {
    if (isEmptyOrNullString(settlementBlockingCode)) {
      return false;
    }
    if (settlementBlockingCode.length() < position) {
      log.info("settlementBlockingCode length is less than or equal to position");
      return false;
    }
    return settlementBlockingCode.charAt(position) == '1';
  }

  /**
   * It parse a json message to String.
   *
   * @param message json string.
   * @return json
   */
  public static String parseEvent(String message) {
    if (StringUtils.isEmpty(message)) {
      return "{}";
    }
    int end = message.lastIndexOf('}');
    int begin = message.indexOf('{');
    if (end == -1 || begin == -1) {
      return "{}";
    }
    return message.substring(begin, end + 1).replaceAll("\\\\?", "");
  }

  /**
   * makeBlankIfNull.
   *
   * @param <T>   generic
   * @param value value
   * @return return
   */
  public static <T> String makeBlankIfNull(T value) {
    if (null == value || "null".equalsIgnoreCase(value.toString())) {
      return "";
    } else {
      return value.toString();
    }
  }

  /**
   * get an alphanumeric string.
   *
   * @param size size
   * @return string data
   */
  public static String getAlphaNumericString(int size) {
    // length is bounded by 256 Character
    var array = new byte[256];
    new SecureRandom().nextBytes(array);
    var randomString = new String(array, StandardCharsets.UTF_8);
    // Create a StringBuffer to store the result
    var result = new StringBuilder();
    // Append first 20 alphanumeric characters
    // from the generated random String into the result
    for (var k = 0; k < randomString.length(); k++) {
      var ch = randomString.charAt(k);
      if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9'))
          && (size > 0)) {
        result.append(ch);
        size--;
      }
    }
    // return the resultant string
    return result.toString();
  }

  /**
   * check if given string is valid int.
   *
   * @param value value
   * @return true/false
   */
  public static boolean checkIfValidInteger(String value) {
    if (isEmptyOrNullString(value)) {
      return false;
    }
    try {
      Integer.parseInt(value);
      return true;
    } catch (Exception e) {
      log.error("not valid integer value");
      return false;
    }
  }

  /**
   * convert bigdecimal to string.
   *
   * @param val values
   * @return string
   */
  public static String convertBigDecimalToString(BigDecimal val) {
    if (val == null) {
      return "0";
    } else {
      return String.valueOf(val);
    }
  }

  /**
   * convert biginteger to string.
   *
   * @param val value
   * @return
   */
  public static String convertBigIntegerToString(BigInteger val) {
    if (val == null) {
      return "0";
    } else {
      return String.valueOf(val);
    }
  }

  /**
   * convert integer to string.
   *
   * @param val val
   * @return
   */
  public static String convertIntegerToString(Integer val) {
    if (val == null) {
      return "0";
    } else {
      return String.valueOf(val);
    }
  }

  /**
   * get comma separated string to list of BigInteger.
   *
   * @param dmsIds ids
   * @return list
   */
  public static List<BigInteger> stringToBigIntList(String dmsIds) {
    var dmsIdList = new ArrayList<BigInteger>();
    for (var val : dmsIds.split(",")) {
      dmsIdList.add(new BigInteger(val));
    }
    return dmsIdList;
  }

  /**
   * checkForMerchantSettlementIdFlags.
   *
   * @param merchantSettlementId merchant settlement id or blocking code.
   * @param index                index
   * @return string reason
   */
  public static Boolean checkForMerchantSettlementIdFlags(String merchantSettlementId, int index) {
    if (null == merchantSettlementId) {
      return false;
    }
    if ((index - 1) >= merchantSettlementId.length()) {
      return false;
    }
    int valueOfFlag = merchantSettlementId.charAt(index - 1) - '0';
    return valueOfFlag != 0;
  }

  /**
   * get last n characters of given string .
   *
   * @param val val
   * @param n   n
   * @return last n characters of given string
   */
  public static String getLastNCharacters(String val, Integer n) {
    var lastNCharacters = "";
    if (StringUtils.isEmpty(val) || val.length() < n) {
      log.info("Please provide valid parameters");
    } else {
      lastNCharacters = val.substring(val.length() - n);
    }
    return lastNCharacters;
  }

  /**
   * get first N characters of given string .
   *
   * @param date date
   * @param n end index
   * @return substring till first n characters
   */
  public static String getFirstNCharacters(String date, Integer n) {
    var firstNCharacters = "";
    if (StringUtils.isEmpty(date) || date.length() < n) {
      log.info("Please provide valid date string");
    } else {
      firstNCharacters = StringUtils.substring(date, 0, n);
    }
    return firstNCharacters;
  }

  public static List<String> getListFromString(String contactList) {
    return Arrays.asList(contactList.split(","));
  }
}
