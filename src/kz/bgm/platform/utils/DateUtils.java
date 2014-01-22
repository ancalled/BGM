package kz.bgm.platform.utils;


import java.text.DateFormat;
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
            installPreviousMonth(cal, 1);
            months.add(cal.getTime());
        }

        return months;
    }


    public static Date getPreviousMonth(Date from, int n) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(from);
        installPreviousMonth(cal, n);
        return cal.getTime();
    }


    public static void installPreviousMonth(Calendar cal, int n) {
        int m = cal.get(Calendar.MONTH);
        if (m - n > cal.getActualMinimum(Calendar.MONTH)) {
            cal.set(Calendar.MONTH, m - n);
        } else {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - n);
            cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
        }
    }


    public static Date getNextMonth(Date from, int n) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(from);
        installNextMonth(cal, n);
        return cal.getTime();
    }


    public static void installNextMonth(Calendar cal, int n) {
        int m = cal.get(Calendar.MONTH);
        if (m + n <= cal.getActualMaximum(Calendar.MONTH)) {
            cal.set(Calendar.MONTH, m + n);
        } else {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + n);
            cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
        }
    }


    public static void main(String[] args) {
        List<Date> months = getMonthsBefore(new Date(), 12, true);

        DateFormat format = new SimpleDateFormat("yyyy MMMMM");
        for (Date m : months) {
            System.out.println(format.format(m));
        }
    }


}
