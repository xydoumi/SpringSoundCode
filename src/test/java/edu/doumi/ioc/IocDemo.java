package edu.doumi.ioc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@RunWith(PowerMockRunner.class)
public class IocDemo {
    @Test
    public void iocEnter(){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
    }
}
