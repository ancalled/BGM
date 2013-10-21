package kz.bgm.platform.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ColorRandom {

    private static final Random RANDOM = new Random();
    private String[] colors = {"#FFCC99", "#FFCCCC", "#CCCCFF", "#99CCFF", "#CCFFFF", "#66FF99", "#CCCCCC",
            "#FF99FF", "#666699", "#33FFFF", "#FFFF66", "#FF99FF", "#9999FF", "#99FFFF", "#99CC99", "#66FF33", "#CCFF00",
            "#CCCC99", "#99CC33", "#66CC33", "#00CCCC", "#FF6600", "#FFFF00", "#6699FF", "#00FF66", "#33FF33"};

    private List<String> workingColors = new ArrayList<>();

    public String getColorName() {
        if (workingColors == null || workingColors.size() == 0) {
            workingColors.addAll(Arrays.asList(colors));
        }


        int seed = workingColors.size() - 1 == 0 ?
                1 : workingColors.size() - 1;

        int randomIdx = RANDOM.nextInt(seed);

        String color = workingColors.get(randomIdx);

        workingColors.remove(randomIdx);

        return color;
    }

    public static void main(String[] args) {
        ColorRandom cr = new ColorRandom();


        for (int i = 1000; i > 0; i--) {
            System.out.println(cr.getColorName());
        }
    }

}

