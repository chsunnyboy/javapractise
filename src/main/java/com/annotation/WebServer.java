package com.annotation;

public class WebServer {
	private String facatory;
	@MiddleWare("tomcat")
	private String name;
	private String type;
	public String getFacatory() {
		return facatory;
	}
	public void setFacatory(String facatory) {
		this.facatory = facatory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
