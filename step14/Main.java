package com.coupang.c4.step14;

import com.coupang.c4.step14.beanfactory.Factory;
import com.coupang.c4.step14.beanfactory.ScopeType;
import com.coupang.c4.step14.beanfactory.SimpleBeanFactory;
import com.coupang.c4.step14.beans.Bean;
import com.coupang.c4.step14.beans.Sample1;
import com.coupang.c4.step14.beans.Sample2;

import java.lang.reflect.InvocationTargetException;


public class Main {
	public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		//생성자로 scope 받음. scope만 prototype으로 바꿔 주면 prototype test 실행.
		Factory simpleBeanFactory
				= new SimpleBeanFactory("/com/coupang/c4/step14/bean-definitions.properties", ScopeType.PROTOTYPE);
		
		Bean instance = simpleBeanFactory.getInstance(Sample2.class);
		System.out.println(instance != null);

		Bean instance2 = simpleBeanFactory.getInstance("sample2");
		System.out.println(instance2 != null);
		System.out.println(Sample2.class.isAssignableFrom(instance2.getClass()));
		System.out.println(instance2 instanceof Sample2);

		Bean instance3 = simpleBeanFactory.getInstance("sample2");
		System.out.println(instance3 != null);
		System.out.println(Sample2.class.isAssignableFrom(instance3.getClass()));
		System.out.println(instance3 instanceof Sample2);

		Bean instance4 = simpleBeanFactory.getInstance("sample1");
		System.out.println(instance4 != null);
		System.out.println(Sample1.class.isAssignableFrom(instance4.getClass()));
		System.out.println(instance4 instanceof Sample1);

		Bean instance5 = simpleBeanFactory.getInstance(Sample1.class);
		System.out.println(instance5 != null);
		System.out.println(Sample1.class.isAssignableFrom(instance5.getClass()));
		System.out.println(instance5 instanceof Sample1);
	}
}
