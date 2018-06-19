package com.dynamicproxy;

import java.lang.reflect.Proxy;

public class Test {
	
	public static void main(String[] args) throws CloneNotSupportedException {
		String a=(String)null;
		System.out.println(a);
		//动态代理
		NewStudentManagerDynamicProxy newStudentManagerDynamicProxy=new NewStudentManagerDynamicProxy();
		NewStudentManagerImpl impl=new NewStudentManagerImpl();
		newStudentManagerDynamicProxy.newProxy(impl);
		NewStudentManager newStudentManager=(NewStudentManager)Proxy.newProxyInstance(NewStudentManagerImpl.class.getClassLoader(), NewStudentManagerImpl.class.getInterfaces(), newStudentManagerDynamicProxy); 
		newStudentManager.addNewStudent("", "");
	}
}

