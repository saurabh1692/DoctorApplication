package io.doctorApp.doc;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	
	
	private String url;
	private String username;
	private String password;
	
	
	public DatabaseConnection(String url, String username, String password) {
		super();
		this.url = url;
		this.username = username;
		this.password = password;
	}





	public Connection getConnection()
	{
	Connection con=null;
	         //String url="jdbc:mysql://localhost:3306/testmysqldb";
	       //  String url="jdbc:mysql://localhost:3306/testmysqdb?zeroDateTimeBehavior=convertToNull";
			
	        // String username="root";
	        // String password="ashish";
	         try
	           {
	        	 Class.forName("com.mysql.jdbc.Driver");
	        	 con=DriverManager.getConnection(url,username,password);
	       	   }		
	         catch(Exception e)
	           {
		         System.out.println(e);
	        	 System.out.println("hello exeception......");
	           }
	         return con;
	}
}

