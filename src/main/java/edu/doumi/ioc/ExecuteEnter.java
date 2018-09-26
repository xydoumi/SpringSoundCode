package edu.doumi.ioc;

import edu.doumi.ioc.person.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks;

public class ExecuteEnter {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        System.out.println("application 加载完成");
        Person student = context.getBean(Person.class);
        student.work();
        /*new Thread(new TestThread()).start();
        new Thread(new TestThread()).start();*/
    }
}

/*
class TestThread implements Runnable{
    public void run() {
        try {
            Animal.Cat.say();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

enum  Animal{
    Cat;
    public void say() throws InterruptedException {
        int count = 1;
        for(int i = 0; i < 10000; i++){
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+":"+count++);
        }
    }
}
*/
