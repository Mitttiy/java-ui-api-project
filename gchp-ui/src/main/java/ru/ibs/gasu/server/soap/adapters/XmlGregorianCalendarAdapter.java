package ru.ibs.gasu.server.soap.adapters;

import java.util.Calendar;
import java.util.TimeZone;

public class XmlGregorianCalendarAdapter {
    public static TimeZone DB_TIMEZONE = TimeZone.getTimeZone("Europe/Moscow");

    public static Long unmarshal(String xmlStr) {
        return javax.xml.bind.DatatypeConverter.parseDateTime(xmlStr).getTimeInMillis();
    }

    /**
     * При передаче даты на бэкенд (в виде календаря), устанавливаем ту таймзону, в которой положено храниться таймстампу в БД
     */
    public static String marshal(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.setTimeZone(DB_TIMEZONE);
        return javax.xml.bind.DatatypeConverter.printDateTime(calendar);
    }
}
