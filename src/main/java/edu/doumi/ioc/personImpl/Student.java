package edu.doumi.ioc.personImpl;

import edu.doumi.ioc.Service.PersonService;
import edu.doumi.ioc.annotation.MyAutowired;
import edu.doumi.ioc.annotation.MyBean;
import edu.doumi.ioc.person.Person;

@MyBean(name="person")
public class Student implements Person {
    @MyAutowired
    private PersonService service;
    private static int count=0;
    public Student(){
        count++;
    }
    public void work() {
        System.out.print("-----"+count+":");
        service.service();
    }
}
