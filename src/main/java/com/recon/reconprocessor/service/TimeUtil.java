package com.recon.reconprocessor.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public final class TimeUtil {

  public static final DateTimeFormatter DEFAULT_DATE_TIME =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");


  private TimeUtil() {
    // private constructor
  }


  /**
   * function to format date.
   *
   * @param date date
   * @param dateTimeFormatter dateTimeFormatter
   * @return string value of date into given format
   */
  public static String formatDate(LocalDateTime date, DateTimeFormatter dateTimeFormatter) {
    if (date == null) {
      date = LocalDateTime.now();
    }
    return date.format(dateTimeFormatter);
  }

  /**
   * function to get data time formatter.
   */
  public static DateTimeFormatter getDateTimeFormatter(String dateFormat) {
    if (isEmptyOrNullString(dateFormat)) {
      return DEFAULT_DATE_TIME;
    } else {
      try {
        return DateTimeFormatter.ofPattern(dateFormat);
      } catch (IllegalArgumentException ex) {
        log.info("IllegalArgumentException for datePattern " + dateFormat);
        return DEFAULT_DATE_TIME;
      }
    }
  }

  public static boolean isEmptyOrNullString(String value) {
    return StringUtils.isEmpty(value) || "null".equalsIgnoreCase(value);
  }
}
