package kz.bgm.platform.utils;


import kz.bgm.platform.model.domain.Quarter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {


    public static List<Date> getMonthsBefore(Date from, int n, boolean completeToQuarter) {

        List<Date> months = new ArrayList<>();
        Calendar cal = new GregorianCalendar();
        cal.setTime(from);

        if (completeToQuarter) {

            int rest = (cal.get(Calendar.MONTH) + 1) % 3;
            if (rest > 0) {
                for (int i = 3 - rest; i > 0; i--) {
                    months.add(getNextMonth(from, i));
                }
            }

            n -= 3 - rest;
        }

        months.add(from);

        for (int i = 1; i < n; i++) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
            months.add(cal.getTime());
        }

        return months;
    }


    public static List<Year> getQuartersBefore(Date from, int quarters) {

        List<Year> yearsList = new ArrayList<>();

        Calendar cal = new GregorianCalendar();
        cal.setTime(from);
        int mnth = cal.get(Calendar.MONTH) + 1;
        int rest = 3 - (mnth % 3);
        Date start = getNextMonth(from, rest);

        Year year = new Year(cal.get(Calendar.YEAR));
        yearsList.add(year);

        for (int q = 0; q < quarters; q++) {
            Quarter quarter = new Quarter();

            for (int m = 0; m < 3; m++) {
                int mntCnt = q * 3 + m;
                Date curr = getPreviousMonth(start, mntCnt);
                quarter.addMonth(new Month(curr));
            }
            quarter.setup();

            if (year.getYear() != quarter.getYear()) {
                year = new Year(quarter.getYear());
                yearsList.add(year);
            }

            year.addQuartes(quarter);
        }

        return yearsList;
    }


    public static Date getPreviousMonth(Date from, int n) {
        return getPreviousMonth(from, n, true);
    }

    public static Date getPreviousMonth(Date from, int n, boolean from1stDay) {
        if (n == 0) return from;
        Calendar cal = new GregorianCalendar();
        cal.setTime(from);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - n);
        if (from1stDay) {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        }
        return cal.getTime();
    }


    public static Date getNextMonth(Date from, int n) {
        return getNextMonth(from, n, true);
    }

    public static Date getNextMonth(Date from, int n, boolean from1stDay) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(from);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + n);
        if (from1stDay) {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        }
        return cal.getTime();
    }


    public static void main(String[] args) throws ParseException {

//        Date from = new Date();
        Date from =  new SimpleDateFormat("yyyy-MM").parse("2014-02");

        int yearsAgo = 3;
        int quartersAgo = yearsAgo * 4;
        List<Year> years = getQuartersBefore(from, quartersAgo);

//        DateFormat format = new SimpleDateFormat("MMMMM");
//        for (Year y : years) {
//            System.out.println(y.getYear());
//            for (Quarter q : y.getQuarters()) {
//                System.out.println("\tQ" + q.getNumber());
//                for (Month m : q.getMonths()) {
//                    System.out.println("\t\t" + format.format(m.getDate()));
//                }
//                System.out.println();
//            }
//        }
//
//        java.sql.Date reportDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("2012-01-23").getTime());

        String[] reportDates = {"2012-01-01", "2013-01-01", "2013-02-01", "2013-04-01", "2013-04-01", "2013-04-01", "2013-04-01", "2013-04-01", "2013-05-01", "2013-05-01", "2013-06-01", "2013-06-01", "2013-07-01", "2013-07-30", "2013-07-30", "2013-07-30", "2013-07-31", "2013-08-01", "2013-08-01", "2013-08-01", "2013-08-01", "2013-09-01", "2013-09-01", "2013-09-01"};

        DateFormat format = new SimpleDateFormat("yyyy MMMMM dd");
        for (String reportDateStr : reportDates) {
            java.sql.Date reportDate = java.sql.Date.valueOf(reportDateStr);
            for (Year y : years) {
                for (Quarter q : y.getQuarters()) {
                    for (Month m : q.getMonths()) {
                        Date monthStart = m.getDate();
                        Date monthEnd = DateUtils.getNextMonth(monthStart, 1);


                        if (!reportDate.before(monthStart) &&
                                reportDate.before(monthEnd)) {
                            System.out.println(reportDate + "\t" + format.format(monthStart) + " - " + format.format(monthEnd));
                        }
                    }

                }
            }
        }
    }


}
