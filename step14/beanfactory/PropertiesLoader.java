package com.coupang.c4.step14.beanfactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by coupang on 2014. 12. 15..
 */
public class PropertiesLoader {
    String propertyPath;
    public PropertiesLoader(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    public String getBeanType(String beanName){
        InputStream is = this.getClass().getResourceAsStream(propertyPath);
        Properties properties = new Properties();
        String beanType=null;

        try {
            properties.load(is);
            beanType = properties.getProperty(beanName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return beanType;
    }

}
