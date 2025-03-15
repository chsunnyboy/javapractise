package com.inheritance;
import org.junit.Test;
public class TestInheritance {

	@Test
	public void main(){
		Insect i = new Bee(1, "red");
		i.attack();
	}
}
