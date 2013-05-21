package kz.bgm.platform.test;

import java.util.ArrayList;
import java.util.List;

public class ListTest {

    public static void main(String[] args) {
        List<String> lst = new ArrayList<>();
        lst.add("one");
        lst.add("two");
        lst.add("three");
        lst.add("fourth");

        lst.remove("three");

        List<String> l2 = new ArrayList<>();
        l2.add("five");
        l2.add("six");
        lst.addAll(l2);
        lst.addAll(l2);
        lst.addAll(l2);

        System.out.println(lst);
    }

}
