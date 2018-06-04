package com.enums;


public enum Test{
	
	RED(1,3), YELLOW(2), BLUE(3); // each is an instance of Color
	
	private int value;
	private int size;

	private Test() {
	}

	private Test(int i) {
		this.value = i;
	}
	private Test(int i,int size) {
		this.value = i;
		this.size=size;
	}

	// define instance method
	public void printValue() {
		System.out.println(this.value);
	}

	public static void main(String[] args) {
		for (Test color : Test.values()) {
			color.printValue();
		}
	}
}
