package com.dynamicproxy;

import java.lang.reflect.Proxy;

public class Test {
	
	public static void main(String[] args) throws CloneNotSupportedException {
//		String a=(String)null;
//		System.out.println(a);
		//动态代理
//		NewStudentManagerDynamicProxy newStudentManagerDynamicProxy=new NewStudentManagerDynamicProxy();
//		NewStudentManagerImpl impl=new NewStudentManagerImpl();
//		newStudentManagerDynamicProxy.newProxy(impl);
//		NewStudentManager newStudentManager=(NewStudentManager)Proxy.newProxyInstance(NewStudentManagerImpl.class.getClassLoader(), NewStudentManagerImpl.class.getInterfaces(), newStudentManagerDynamicProxy); 
//		newStudentManager.addNewStudent("", "");
		Person p = new Person(23, "zhang");  
		Person p1 = (Person) p.clone();    
		
		String result = p.getName() == p1.getName()? "clone是浅拷贝的" : "clone是深拷贝的";  
	}
}
class Person implements Cloneable{  
      
    private int age ;  
    private String name;  
     
    public Person(int age, String name) {  
        this.age = age;  
       this.name = name;  
   }  
      
    public Person() {}  
  
    public int getAge() {  
        return age;  
    }  
  
    public String getName() {  
       return name;  
   }   
    public Person clone() throws CloneNotSupportedException {
    	return (Person)super.clone();
    }
}  

