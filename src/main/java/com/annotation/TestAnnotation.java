package com.annotation;

import java.lang.reflect.Field;

public class TestAnnotation {
	public static void main(String[] args) {
		WebServer ws=new WebServer();
		ws.setName("webLogic");
		System.out.println(ws.getName());
		Field[] md=ws.getClass().getFields();
		for(Field f:md) {
			if(f.isAnnotationPresent(MiddleWare.class)) {
				System.out.println("------");
				MiddleWare middleWare=f.getAnnotation(MiddleWare.class);
				System.out.println(middleWare.value());
			}
		}
		System.out.println(md);
	}
}
