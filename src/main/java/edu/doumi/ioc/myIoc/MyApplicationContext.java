package edu.doumi.ioc.myIoc;

import edu.doumi.ioc.utils.MyBeanUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class MyApplicationContext implements MyBeanFactory {
    // 存放bean构建信息
    protected Map<String, MyBeanDefinition> myBeanFactory = new HashMap<String, MyBeanDefinition>();
    // 存放初始化后的单例bean对象
    protected Map<String,Object> context = new HashMap<String,Object>();
    public Object getBean(String beanName){
        if(null == myBeanFactory || myBeanFactory.size() == 0 || !myBeanFactory.containsKey(beanName)){
            Mylog.info("没有配置Bean信息");
            return null;
        }
        // 如果已经初始化直接从context容器中获取
        Object existBean = context.get(beanName);
        if(null == existBean){
            return createBean(beanName);
        }
        return existBean;
    }

    public Object createBean(String beanName){
        // 获取bean信息
        MyBeanDefinition definition =  myBeanFactory.get(beanName);
        Class cls;
        try {
            cls = Class.forName(definition.getClassName());
        } catch (ClassNotFoundException e) {
            Mylog.info("not this class:"+definition.getClassName());
            throw new RuntimeException("not this class:"+definition.getClassName());
        }
        Object beanInstance = MyBeanUtil.instantiation(cls);
        if(definition.getPropertise() != null && definition.getPropertise().size()>0){
            for(Property p:definition.getPropertise()){
                if(p.getValue()!=null){
                    MyBeanUtil.setProperty(cls, beanInstance, p.getName(), p.getValue());
                }
                if(p.getRef()!=null){
                    MyBeanUtil.setProperty(cls, beanInstance, p.getName(), getBean(p.getRef()));
                }
            }
        }
        // 单例放入容器
        if(MyBeanDefinition.SINGLETON.equalsIgnoreCase(definition.getScope())){
            context.put(definition.getBeanName(), beanInstance);
        }
        return beanInstance;
    }

    void singletonInit(){
        if(myBeanFactory == null || myBeanFactory.size()==0){
            Mylog.info("not config bean xml");
            return;
        }
        // 实例化单例，默认为单例
        for(Map.Entry<String, MyBeanDefinition> b : myBeanFactory.entrySet()){
            if(null == b.getValue().getScope() || MyBeanDefinition.SINGLETON.equals(b.getValue().getScope()))
                createBean(b.getValue().getBeanName());
        }
    }
}
