package edu.doumi.ioc;

import edu.doumi.ioc.myIoc.ClasspathXMLApplication;
import edu.doumi.ioc.myIoc.MyApplicationContext;
import edu.doumi.ioc.myIoc.Mylog;
import edu.doumi.ioc.person.Person;
import edu.doumi.ioc.personImpl.Student;
import edu.doumi.ioc.utils.MyStringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class IocDemo {
    @Test
    public void iocEnter(){
        MyApplicationContext context = new ClasspathXMLApplication("/application.xml");
        Person p1 = (Person)context.getBean("person");
        p1.work();
        Person p2 = (Person)context.getBean("person");
        p2.work();
    }
}
