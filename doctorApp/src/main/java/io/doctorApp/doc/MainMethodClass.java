package io.doctorApp.doc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;



@SpringBootApplication

public class MainMethodClass extends SpringBootServletInitializer {
	
	
	 @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(MainMethodClass.class);
	    }
	public static void main(String args[]) {
		SpringApplication.run(MainMethodClass.class,args);
	}

}
