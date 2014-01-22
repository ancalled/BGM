package kz.bgm.platform.utils;


import java.util.*;

public class DateUtils {



    public static List<Date> getMonthsBefore(Date from, int n) {

        List<Date> months = new ArrayList<>();
        Calendar cal = new GregorianCalendar();
        cal.setTime(from);

        months.add(from);

        for (int i = 1; i < n; i++) {
            int currMnth = cal.get(Calendar.MONTH);
            if (currMnth > cal.getActualMinimum(Calendar.MONTH)) {
                cal.set(Calendar.MONTH, currMnth - 1);
            } else {
                cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
                cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
            }

            months.add(cal.getTime());
        }

        return months;

    }

}
