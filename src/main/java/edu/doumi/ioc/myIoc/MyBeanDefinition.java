package edu.doumi.ioc.myIoc;

import java.util.ArrayList;
import java.util.List;

public class MyBeanDefinition {
    public final static String SINGLETON = "singleton";
    public final static String PROTOTYPE = "prototype";
    private String beanName;
    private String className;
    private String scope;
    private List<Property> properties;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<Property> getPropertise(){
        return properties;
    }

    public void setPropertise(Property property){
        if(properties == null)
            properties = new ArrayList<Property>();
        properties.add(property);
    }

    public boolean isSingleton(){
        return SINGLETON.equalsIgnoreCase(scope);
    }
}
