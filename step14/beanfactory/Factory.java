package com.coupang.c4.step14.beanfactory;

import com.coupang.c4.step14.beans.Bean;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by coupang on 2014. 12. 15..
 */
public interface Factory {
    public <T> Bean getInstance(Class<T> type) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
    public Bean getInstance(String beanName) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException;


}
