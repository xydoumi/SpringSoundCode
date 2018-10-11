package edu.doumi.ioc.utils;

public class MyStringUtil {
    public static String toLowerFirst(String oldStr){
        char[] temp = oldStr.toCharArray();
        temp[0] = (char)(temp[0]+32);
        return String.valueOf(temp);
    }
}
