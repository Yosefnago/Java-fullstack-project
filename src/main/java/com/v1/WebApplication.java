package com.v1;


import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;



@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
public class WebApplication {

	public static void main(String[] args) {

		SpringApplication.run(WebApplication.class, args);


	}

}
