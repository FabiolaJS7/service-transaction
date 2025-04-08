package com.bootcamp.service.transaction.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    private static  final LocalDate NOW = LocalDate.now();
    private static  final LocalDate START_OF_MONTH = NOW.withDayOfMonth(1);
    private static  final LocalDate END_OF_MONTH = NOW.withDayOfMonth(NOW.lengthOfMonth());

    private static  final int END_HOUR = 23;
    private static  final int END_MINUTE = 59;
    private static  final int END_SECOND = 59;

    public static Date getStartDate() {
        return Date.from(START_OF_MONTH.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date getEndDate() {
        return Date.from(END_OF_MONTH.atTime(END_HOUR, END_MINUTE, END_SECOND).atZone(ZoneId.systemDefault()).toInstant());
    }
}
