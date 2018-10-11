package edu.doumi.ioc.ServiceImpl;

import edu.doumi.ioc.Service.PersonService;
import edu.doumi.ioc.annotation.MyBean;

@MyBean(name="personService")
public class StudentService implements PersonService {
    public void service() {
        System.out.println("good good study,day day up!!!!");
    }
}
