package kz.bgm.platform.model.domain;

import kz.bgm.platform.utils.Month;

import java.util.*;


public class Quarter {

    private int number;
    private int year;

    private List<Month> months = new ArrayList<>();

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Month> getMonths() {
        return months;
    }

    public void addMonth(Month m) {
        months.add(m);
    }

    public void setup() {
        if (!months.isEmpty()) {
            Month first = months.get(0);
            Calendar cal = new GregorianCalendar();
            cal.setTime(first.getDate());

            number = (cal.get(Calendar.MONTH) / 3) + 1;
            year = cal.get(Calendar.YEAR);

            Collections.sort(months, new Comparator<Month>() {
                @Override
                public int compare(Month o1, Month o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
        }
    }
}
