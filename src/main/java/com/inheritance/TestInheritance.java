package com.inheritance;
import org.junit.Test;
public class TestInheritance {

	@Test
	public static void main(String[] args){
		Insect i = new Bee(1, "red");
		i.attack();
	}
}
