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

  public static final String SIMPLE_DATE_PATTERN = "yyyy-MM-dd";
  public static final DateTimeFormatter DATE_FORMAT_yyyyMMdd =
      DateTimeFormatter.ofPattern("yyyyMMdd");

  public static final DateTimeFormatter DATE_FORMAT_yyyy_MM_dd =
      DateTimeFormatter.ofPattern(SIMPLE_DATE_PATTERN);
  public static final DateTimeFormatter DATE_FORMAT_yyyy_MM_dd_HH_mm_ss_SSS =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public static final DateTimeFormatter DATE_FORMAT_yyyyMMddHHmm =
      DateTimeFormatter.ofPattern("yyyyMMddHHmm");
  public static final DateTimeFormatter DATE_FORMAT_ddMMyy = DateTimeFormatter.ofPattern("ddMMyy");
  public static final DateTimeFormatter TIME_CUTOFF_FORMAT = DateTimeFormatter.ofPattern("HHmm");
  public static final DateTimeFormatter DEFAULT_DATE_TIME =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public static final String DATEFORMAT_WITH_MS = "yyyy-MM-dd HH:mm:ss.SSS";
  private static final int EIGHT = 8;
  private static final char HOUR = 'H';
  private static final char MINUTE = 'M';
  public static final DateTimeFormatter DATE_FORMAT_yyyyMMddHHmmss =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
  public static final DateTimeFormatter DATE_FORMAT_DDMMYYYYHHMMSSSSS =
      DateTimeFormatter.ofPattern("ddMMyyyyHHmmssSSS");
  public static final DateTimeFormatter DATE_FORMAT_DDMMYYYYHHMMSS =
      DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");

  private TimeUtil() {
    // private constructor
  }

  /**
   * Example : For 2021-02-03 09:57:10, function returns 0957.
   *
   * @param dateTime date
   * @return date as String in HHMM
   */
  public static String getTimeAsString(LocalDateTime dateTime) {
    var transactionHour = String.valueOf(dateTime.getHour());
    transactionHour = transactionHour.length() == 1 ? "0" + transactionHour : transactionHour;
    var transactionMinute = String.valueOf(dateTime.getMinute());
    transactionMinute =
        transactionMinute.length() == 1 ? "0" + transactionMinute : transactionMinute;
    return transactionHour + transactionMinute;
  }

  /**
   * Example : For 2021-02-03 09:00:00 & 2021-02-03 12:00:00 function returns 3.
   *
   * @param from from
   * @param to to
   * @return number of hrs difference
   */
  public static long diffInDatesInHours(LocalDateTime from, LocalDateTime to) {
    if (from != null && to != null) {
      return from.until(to, ChronoUnit.HOURS);
    }
    return 0L;
  }

  /**
   * get localDateTime from Date object.
   *
   * @param dateToConvert date object
   * @return localDateTime
   */
  public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
    return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }


  public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
    return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  /**
   * format date.
   *
   * @param date localDate (here time is not there)
   * @param dateTimeFormatter formatter
   * @return string value of date into given format
   */
  public static String formatDate(LocalDate date, DateTimeFormatter dateTimeFormatter) {
    if (date == null) {
      date = LocalDate.now();
    }
    return date.format(dateTimeFormatter);
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
   * format dateTime.
   *
   * @param dateTime localDate (here time is not there)
   * @param dateTimeFormatter formatter
   * @return string value of date into given format
   */
  public static String formatDateTime(LocalDateTime dateTime, DateTimeFormatter dateTimeFormatter) {
    if (dateTime == null) {
      dateTime = LocalDateTime.now();
    }
    return dateTime.format(dateTimeFormatter);
  }

  /**
   * format dateTime.
   *
   * @param localDate localDate (here time is not there)
   * @param dateTimeFormatter formatter
   * @return date into given format
   */
  public static Date getDateFromLocalDate(LocalDate localDate, String dateTimeFormatter) {
    Date date = null;
    try {
      date = new SimpleDateFormat(dateTimeFormatter, Locale.ENGLISH).parse(localDate.toString());
    } catch (Exception e) {
      log.error("Exception occurred while parsing local date: {}", e.getMessage());
    }
    return date;
  }

  /**
   * get hhmm as int from the given localDateTime.
   *
   * @param time LocalDateTime object
   * @return int hhmm
   */
  public static Integer getTimeAsInteger(LocalDateTime time) {
    time = time == null ? LocalDateTime.now() : time;
    return time.getHour() * 100 + time.getMinute();

  }

  /**
   * get yyyyMMdd as String from the given string.
   *
   * @param dateTimeString dateTimeString in yyyyMMdd format
   * @return LocalDate
   */
  public static LocalDate getDateFromInteger(Integer dateTimeString) {
    if (dateTimeString.toString().length() != EIGHT) {
      return LocalDate.now();
    }
    int year = dateTimeString / 10000;
    int month = (dateTimeString % 10000) / 100;
    int day = dateTimeString % 100;
    return LocalDate.of(year, month, day);
  }

  /**
   * Provide date Minus the date provided.
   *
   * @param date dateTimeString in yyyyMMdd format
   * @param days number of days to be subtracted
   * @return LocalDate
   */
  public static LocalDate getNDaysBackDate(LocalDate date, Integer days) {
    return date.minusDays(days);
  }

  /**
   * getting n days back.
   *
   * @param days days
   * @return
   */
  public static Date getNDaysBackDate(Integer days) {
    var cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -(days));
    cal.set(Calendar.HOUR, 0);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }


  /**
   * get n minutes back from given dateTime.
   *
   * @param dateTime dateTime
   * @param minutes number of minutes to subtract
   * @return result
   */
  public static LocalDateTime getNMinutesBackTimeFromDateTime(LocalDateTime dateTime,
                                                              Integer minutes) {
    return dateTime.minusMinutes(minutes);
  }

  /**
   * get n days ahead date from given date.
   *
   * @param date date
   * @param days days to go ahead
   * @return result
   */
  public static LocalDate getNDaysAheadDateFromDate(LocalDate date, long days) {
    return date.plusDays(days);
  }

  /**
   * get a start time say 10:00 and end time say 16:00 and a frequency say 2H.
   *
   * @param startTime starting
   * @param endTime ending
   * @param frequency String 2H, 20M etc
   * @return result list of String based on frequency [1000, 1200, 1400, 1600]
   */
  public static List<String> getTimeCutOffsForFrequency(LocalTime startTime, LocalTime endTime,
                                                        String frequency) {
    var timeUnit = frequency.charAt(frequency.length() - 1);
    var frequencyValue = Long.valueOf(frequency.substring(0, frequency.length() - 1));
    List<String> result = new ArrayList<>();
    if (timeUnit == HOUR) {
      LocalTime time = startTime;
      while (time.compareTo(endTime) <= 0) {
        result.add(time.format(TIME_CUTOFF_FORMAT));
        time = time.plusHours(frequencyValue);
      }
    } else if (timeUnit == MINUTE) {
      LocalTime time = startTime;
      while (time.compareTo(endTime) <= 0) {
        result.add(time.format(TIME_CUTOFF_FORMAT));
        time = time.plusMinutes(frequencyValue);
      }
    }
    return result;
  }

  /**
   * function to get day name.
   *
   * @param day day
   * @return day name
   */
  public static String getWeekday(int day) {
    Map<Integer, String> daysName = new HashMap<>();
    daysName.put(0, "Monday");
    daysName.put(1, "Tuesday");
    daysName.put(2, "Wednesday");
    daysName.put(3, "Thursday");
    daysName.put(4, "Friday");
    daysName.put(5, "Saturday");
    daysName.put(6, "Sunday");
    return daysName.get(day);
  }

  /**
   * Method to return local data of the given time.
   *
   * @param time 12:12:12
   * @return localDateTimeObject
   */
  public static LocalTime getLocalTime(String time) {
    String[] values = time.split(":");
    return LocalTime.of(Integer.parseInt(values[0]), Integer.parseInt(values[1]),
        Integer.parseInt(values[2]));
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

  /**
   * get n days ahead date from given date.
   *
   * @param dateTime date
   * @param days days to go ahead
   * @return result
   */
  public static LocalDateTime getNDaysAheadDateFromDateTime(LocalDateTime dateTime, Long days) {
    return dateTime.plusDays(days);
  }

  public static LocalDateTime getMidnightDate(LocalDate date) {
    return LocalDateTime.of(date, LocalTime.MIDNIGHT.minusSeconds(1L));
  }

  public static Date getDateFromString(String date) {
    var ldt = LocalDateTime.parse(date, DATE_FORMAT_yyyy_MM_dd_HH_mm_ss_SSS);
    return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
  }

  /**
   * get difference in dates .
   *
   * @param date1 date1
   * @param date2 date2
   * @return difference in dates
   */
  public static Integer getDifferenceInDays(String date1, String date2) {
    long days = ChronoUnit.DAYS.between(LocalDate.parse(date1), LocalDate.parse(date2));
    return (int) days;
  }

  /**
   * convert settlementId to yyyy-MM-dd HH:mm:ss format.
   *
   * @param settlementId settlementId
   * @return
   */
  public static String convertSettlementIdToLocalDateTimeInStringFormat(String settlementId) {
    return TimeUtil.formatDateTime(
        LocalDateTime.parse(settlementId, TimeUtil.DATE_FORMAT_yyyyMMddHHmm),
        TimeUtil.DATE_FORMAT_yyyy_MM_dd_HH_mm_ss_SSS);

  }

  /**
   * validate 2 dates function if they, are after on another.
   *
   * @param startDate startDate
   * @param endDate endDate
   * @return if valid date
   */
  public static Boolean validateDate(String startDate, String endDate) {
    try {
      if (startDate == null || endDate == null) {
        return false;
      }
      var startLocalDate = LocalDate.parse(startDate);
      var endLocalDate = LocalDate.parse(endDate);
      if (startLocalDate.isAfter(endLocalDate)) {
        return false;
      }
      if (startLocalDate.isBefore(LocalDate.now())) {
        return false;
      }
      var nextYear = LocalDate.now().plusYears(1);
      return !endLocalDate.isAfter(nextYear);
    } catch (Exception e) {
      log.info("Exception occurred while parsing startDate: {} endDate: {} ", startDate, endDate);
      return false;
    }
  }
}
