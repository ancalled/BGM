package kz.bgm.platform.utils;

import kz.bgm.platform.model.domain.Quarter;

import java.util.ArrayList;
import java.util.List;


public class Year {

    private int year;

    private List<Quarter> quarters = new ArrayList<>();

    public Year(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public List<Quarter> getQuarters() {
        return quarters;
    }

    public void addQuartes(Quarter q) {
        quarters.add(q);
    }
}
