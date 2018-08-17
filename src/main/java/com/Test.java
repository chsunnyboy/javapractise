package com;

import java.sql.Connection;
import java.sql.DriverManager;

public class Test {

	public static void main(String[] args) {
//		String host = "jdbc:postgresql://localhost:5432/bugsaas";
//	    String username = "saas_ibs_dev";
//	    String password = "saas_ibs_dev";
//	    Connection c = null;
//	    try {
//	        Class.forName("org.postgresql.Driver");
//	        c = DriverManager.getConnection(host, username, password);
//	        c.prepareStatement("select * FROM information_schema.sql_features;");
//	       
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        System.err.println(e.getClass().getName() + ": " + e.getMessage());
//	        System.exit(0);
//	    }
//	    System.out.println("Opened database successfully");
		String a="1,2,3,5,6,8,";
		System.out.println(a.substring(0,a.length()));
	}

}
