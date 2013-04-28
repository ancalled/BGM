package kz.bgm.platform.test.reflection;


import kz.bgm.platform.model.domain.CalculatedReportItem;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
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


    private boolean checkMethodType(Method m, Class type) {
        Type mType = m.getGenericReturnType();
        return mType == type;


    }

    @Test
    public void testMethods() throws InvocationTargetException, IllegalAccessException, InstantiationException {

        Class cl = CalculatedReportItem.class;
        CalculatedReportItem clClone = (CalculatedReportItem) cl.newInstance();
        List<Method> getters = getGetters(cl);

        Assert.assertFalse(getters.isEmpty());

        for (Method m : getters) {
            Object o = m.invoke(clClone);
            System.out.println(o);
            System.out.println(m.getName());
            System.out.println(m.getGenericReturnType());

            Assert.assertTrue(checkMethodType(m, Object.class));

        }


    }
}
