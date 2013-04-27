package kz.bgm.platform.test.reflection;


import kz.bgm.platform.model.domain.CalculatedReportItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionTest {

    private List<Method> getGetters(Class cls) {
        Method[] metMass = cls.getMethods();
        List<Method> methodList = new ArrayList<Method>();

        for (Method m : metMass) {
            if (m.getName().startsWith("get")) {
                methodList.add(m);
            }
        }
        return methodList;
    }


    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        ReflectionTest reflect = new ReflectionTest();

        Class cl = CalculatedReportItem.class;
        CalculatedReportItem clClone = (CalculatedReportItem) cl.newInstance();
        List<Method> getters = reflect.getGetters(cl);

        for (Method m : getters) {
            Object o = m.invoke(clClone);
            System.out.println(o);
            System.out.println(m.getName());
            System.out.println(m.getGenericReturnType());
        }

    }
}
