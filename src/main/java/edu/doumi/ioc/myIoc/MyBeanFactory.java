package edu.doumi.ioc.myIoc;

import javax.xml.parsers.ParserConfigurationException;

public interface MyBeanFactory {
    Object getBean(String beanName);
    void loadBean();
}
