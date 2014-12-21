package com.coupang.c4.step14.beanfactory;


/* 1,1-1만 하면 됨. (step16에서 구현하고 있음) 배점 기준="유연성"과 "확장성"을 잘 고려하여 simpleBeanFactory를 잘 나누고 추상화하면 된다!!!
100점:감동
90점:spec 완성
70점:일부 완성
30점:하나도 못함
다음 주 금요일까지(12월 19일)
step14만 따로 프로젝트 생성해서 과제한 후, git에 올린 후 git url 이메일로 보내주면 됨.

전제: 기본 생성자만존재, 다른 소스파일 다 고쳐도 됨.

1. singleton instance 관리:항상 동일한 인스턴스를 리턴하도록 (동일한 bean type, beanName이 들어오면 항상 같은 인스턴스 리턴)
- 방법: 생성된 bean 캐싱-멤버변수로 map을 두고 key를 type으로 두고, value를 name으로 두면 됨.

	1-1. 고려 내용: 추후 다른 scope 생성이 용이한 구조가 되도록.-step17의 chainOfResponsibility처럼 구현하면 됨.
	(1.SINGLETON scope: 무조건 동일한 인스턴스 생성 2.PROTOTYPE scope: 언제어디서나 다른 인스턴스가 생성 3.~~scope 등등 어떻게 클래스를 분리해서 다른 scope도 쉽게 추가할 수 있도 고민)
	if-else 같은 거 쓰지말고, scope 타입을 따로 관리해주는 인터페이스 구현하여 용이하게 만들어 보자.

2.thread safe 하게 구성할 것: beanfactory는 일반적으로 getinstance를 막 호출하게되는데 어떻게 동기화할지 고민하여 만들어 보자

3.계층 구조가 가능한 bean factory: 상속을 쓰는게 아니라 우리가 사용하는 beanfactory가 있고, 다른데서는 singleton type을 받는 beanfactory, prototype beanfactory 같은 것을 만들어서
서로 대화하면서 "이 type 처리할 수 있나"같은 물음 던져서 서로 넘겨주는 것 구현하자.
 */



/**
 * Dead Line : 12 / 19 (Fri)
 * Step 14만 따로 해서 git에 올린 후, 메일로 주소를 보냄.
 *
 * 전제조건:
 *  - 기본 생성자가 있는 bean만 취급한다. declaredConstro를 사용.
 * 1. Singleton instance 관리 - 생성된 bean 캐싱 : Map 사용(멤버 변수)
 * 1-1. 고려 내용 추후 다른 scope 생성이 용이한 구조가 되도록. : 다른 scope을 구현하라는 얘기는 아님. OOP로 개발한다는 것은 어떻게 하는 것인가?
 *
 * 유연성과 확장성을 잘 고려해서 심플 빈팩토리를 잘 나누고 추상화를 해줘야 함.
 * 점수기준 - 감동을 받으면 100점, 스펙 완성 : 90점, 일부 완성 : 70점, 미구현 : 30점
 * 16번에서 scope로 풀고 있음.
 * 새 method와 return으로 해결하시오. 불필요한 else문을 만들지 마시오.
 * 다중 루프문은 각 스탭별로 메소드를 분리.
 * 인터페이스를 만들고 이것을 기준으로 움직이는 것이 유리
 *
 * 목적: 인스턴스의 생성을 위임하고 싶어서 만든것.
 */

import com.coupang.c4.step14.beans.Bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

public class SimpleBeanFactory implements Factory {
	private String propertyPath;
	private ScopeType scope;
	private Bean resultBean;
	private ArrayList<Bean> list = new ArrayList<Bean>();
	private PropertiesLoader propertiesLoader;

	public SimpleBeanFactory(String propertyPath, ScopeType scope) {
		this.propertyPath = propertyPath;
		this.scope = scope;
		propertiesLoader = new PropertiesLoader(propertyPath);
	}

	public <T> Bean getInstance(Class<T> beanType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		// TODO : 코드를 채워주세요
		Class<?> clazz = beanType;

		if(scope == ScopeType.SINGLETON) {
			resultBean = doSingleTon(clazz);
		}
		if(scope == ScopeType.PROTOTYPE){
			resultBean = doProtoType(clazz);
		}

		return resultBean;
	}

	public Bean getInstance(String beanName) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException {
		// TODO : 코드를 채워주세요
		String beanType;
		beanType = propertiesLoader.getBeanType(beanName);
		Class<?> clazz = Class.forName(beanType);

		if(scope == ScopeType.SINGLETON) {
			resultBean = doSingleTon(clazz);
		}
		if(scope == ScopeType.PROTOTYPE){
			resultBean = doProtoType(clazz);
		}

		return resultBean;
	}

//	중복되는 코드들 메소드로 빼놓기
	private Bean doSingleTon(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Bean findBean;
		Iterator iterator = list.iterator();
		Constructor<?> declaredConstructor = null;

		while (iterator.hasNext()) {
			findBean =  (Bean)iterator.next();
			if (findBean.getClass() == clazz) {
				System.out.println("SINGLETON: 이미 인스턴스가 존재합니다.");
				return findBean;
			}
		}
		System.out.println("SINGLETON: 인스턴스를 새로 생성 합니다.");
		declaredConstructor = clazz.getDeclaredConstructor();
		declaredConstructor.setAccessible(true);
		findBean = (Bean) declaredConstructor.newInstance();

		list.add(findBean);

		return findBean;

	}

	private Bean doProtoType(Class clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Bean getBean;
		Constructor<?> declaredConstructor = null;

		declaredConstructor = clazz.getDeclaredConstructor();
		declaredConstructor.setAccessible(true);
		getBean = (Bean) declaredConstructor.newInstance();

		System.out.println("PROTOTYPE: 인스턴스 생성!!");

		return getBean;
	}
}
