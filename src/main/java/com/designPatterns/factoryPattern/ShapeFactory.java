package com.designPatterns.factoryPattern;

public class ShapeFactory {
	 /** 
	  * 使用 getShape 方法获取形状类型的对象
	  * 使用场景 日志记录器  数据库访问  设计连接服务器的框架
	  * 复杂对象使用工厂模式，简单对象使用new关键字创建
	  * 优点：扩展性高，屏蔽具体的实现，可根据名字创建对象；
	  *	缺点：类的个数增加，增加系统复杂度，增加系统具体类的依赖
	 */
	   public Shape getShape(String shapeType){
	      if(shapeType == null){
	         return null;
	      }        
	      if(shapeType.equalsIgnoreCase("CIRCLE")){
	         return new Circle();
	      } else if(shapeType.equalsIgnoreCase("RECTANGLE")){
	         return new Rectangle();
	      } else if(shapeType.equalsIgnoreCase("SQUARE")){
	         return new Square();
	      }
	      return null;
	   }
}
