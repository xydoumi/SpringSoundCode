package edu.doumi.ioc.utils;

import edu.doumi.ioc.myIoc.Mylog;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MyBeanUtil {
    public static Object instantiation(Class cls){
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            Mylog.info(e.getMessage());
            throw new RuntimeException("实例化失败");
        } catch (IllegalAccessException e) {
            Mylog.info(e.getMessage());
            throw new RuntimeException("实例化失败");
        }
    }

    public static void setProperty(Class cls, Object bean, String propertyName, Object value){
        Field f = null;
        try {
            f = cls.getDeclaredField(propertyName);
        } catch (NoSuchFieldException e) {
            Mylog.info("bean is not such field:"+f.getType());
            throw new RuntimeException("bean is not such field:"+f.getType());
        }
        if(null != f){
            try {
                if(Modifier.isPrivate(f.getModifiers()))
                    f.setAccessible(true);
                f.set(bean,value);
            } catch (IllegalAccessException e) {
                Mylog.info("not match type:"+f.getType());
                throw new RuntimeException("not match type:"+f.getType());
            }
        }
    }
}
