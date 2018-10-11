package edu.doumi.ioc.myIoc;


import edu.doumi.ioc.annotation.MyAutowired;
import edu.doumi.ioc.annotation.MyBean;
import edu.doumi.ioc.utils.MyStringUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;


public class ClasspathXMLApplication extends MyApplicationContext{
    private String classpath;
    public ClasspathXMLApplication(String classpath){
        this.classpath = classpath;
        loadBean();
        singletonInit();
    }
    public void loadBean(){
        InputStream is = null;
        List<Element> scanLabel = null;
        try {
            SAXReader reader=new SAXReader();
            is = ClasspathXMLApplication.class.getResourceAsStream(classpath);
            Document doc = reader.read(is);
            List<Element> beanList =  doc.selectNodes("//bean");
            scanLabel = doc.selectNodes("//component-scan");
            for(Element bean:beanList){
                MyBeanDefinition beanDefinition = new MyBeanDefinition();
                String name = bean.attributeValue("name");
                String classname = bean.attributeValue("class");
                String scope = bean.attributeValue("scope");
                beanDefinition.setBeanName(name);
                beanDefinition.setClassName(classname);
                if(null!=scope)
                    beanDefinition.setScope(scope);
                List<Element> properties = bean.elements();
                for(Element property:properties){
                    Property p = new Property();
                    String pname = property.attributeValue("name");
                    String pvalue = property.attributeValue("value");
                    String pref = property.attributeValue("ref");
                    p.setName(pname);
                    p.setValue(pvalue);
                    p.setRef(pref);
                    beanDefinition.setPropertise(p);
                }
                myBeanFactory.put(beanDefinition.getBeanName(),beanDefinition);
            }
        } catch (org.dom4j.DocumentException e) {
            e.printStackTrace();
        }finally {
            if(null!=is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 如果有扫描标签开始扫描包名
        if(null!=scanLabel){
            List<String> packages = new LinkedList<String>();
            // 装载包名
            for(Element scan:scanLabel){
                String packageString = scan.attributeValue("back_package");
                String[] packageStrArr = packageString.split(",");
                packages.addAll(Arrays.asList(packageStrArr));
            }
            // 解析器包中注解
            parserAnnotation(packages);
        }
    }
    void parserAnnotation(List<String> packages){
        AnnotationParser annotationParser = new AnnotationParser(packages);
        annotationParser.parser();
    }
    //注解解析器
    class AnnotationParser{
        //所需扫描的包名
        private List<String> back_package;
        // 需要扫描的所有类名
        private List<String> classNames;
        AnnotationParser(List<String> back_package){
            this.back_package = back_package;
        }
        void parser(){
            classNames = findScanClass();
            // 1、根据包名创建类
            //    2、反射确实是否被注解标识
            //       3、根据注解标识自动注入
            for(String clsName:classNames){
                try{
                    Class cls = Class.forName(clsName);
                    if(cls.isAnnotationPresent(MyBean.class)){
                        MyBeanDefinition beanDefinition = new MyBeanDefinition();
                        MyBean myBean = (MyBean)cls.getAnnotation(MyBean.class);
                        // 默认为类名首字母小写
                        if("".equals(myBean.name())){
                            beanDefinition.setBeanName(MyStringUtil.toLowerFirst(cls.getSimpleName()));
                        }else{
                            beanDefinition.setBeanName(myBean.name());
                        }
                        beanDefinition.setScope(myBean.scope());
                        beanDefinition.setClassName(clsName);
                        Field[] fields = cls.getDeclaredFields();
                        for(Field field:fields){
                            if(field.isAnnotationPresent(MyAutowired.class)) {
                                Property property = new Property();
                                property.setName(field.getName());
                                MyAutowired wiredInof = (MyAutowired) field.getAnnotation(MyAutowired.class);
                                String refName = wiredInof.name();
                                if ("".equals(refName)) {
                                    Class refCls = field.getType();
                                    property.setRef(MyStringUtil.toLowerFirst(refCls.getSimpleName()));
                                } else {
                                    property.setRef(refName);
                                }
                                beanDefinition.setPropertise(property);
                            }
                        }
                        myBeanFactory.put(beanDefinition.getBeanName(),beanDefinition);
                    }
                }catch(Exception e){
                    Mylog.info(e.getMessage());
                }
            }
        }

        List<String> findScanClass(){
            List<String> classNames = new LinkedList<String>();
            for(String packageName:back_package){
                classNames.addAll(findClassNames(packageName));
            }
            return classNames;
        }

        private List<String> findClassNames(String packageName) {
            List<String> classNames = new LinkedList<String>();
            String packageUrl = packageName.replace(".","/");
            try {
                Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageUrl);
                while(urls.hasMoreElements()){
                    URL url = urls.nextElement();
                    String type  = url.getProtocol();
                    if("file".equalsIgnoreCase(type)){
                        classNames.addAll(findClassNamesByFile(url.getPath(),true));
                    }else{
                        classNames.addAll(findClassNamesByJar(url.getPath(),true));
                    }

                }
            } catch (IOException e) {
                Mylog.info(e.getMessage());
                throw new RuntimeException("null this path");
            }
            return classNames;
        }

        private List<String> findClassNamesByFile(String filePath, boolean isChild){
            List<String> classNames = new LinkedList<String>();
            File file = new File(filePath);
            File[] childFiles = file.listFiles();
            for(File childFile:childFiles){
                String path = childFile.getPath();
                String classname = path.substring(path.indexOf("classes\\")+8,path.indexOf(".class")).replace("\\",".");
                classNames.add(classname);
            }
            return classNames;
        }
        private List<String> findClassNamesByJar(String packageName, boolean isChild){
            return null;
        }

    }
}
