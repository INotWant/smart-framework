package utils;

import org.junit.Assert;
import org.junit.Test;
import top.inotwant.utils.ClassUtil;

import java.util.Set;

public class TestClassUtil {

    @Test
    public void testLoadClass() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("top.inotwant.ConfigConstant");
        Class<?> clazz = ClassUtil.loadClass("top.inotwant.ConfigConstant");
        Assert.assertEquals(aClass, clazz);
    }

    @Test
    public void testLoadClassSet(){
        Set<Class<?>> clazzs = ClassUtil.loadClassSet("top.inotwant");
        for (Class<?> clazz : clazzs){
            System.out.println(clazz);
        }
    }

}
