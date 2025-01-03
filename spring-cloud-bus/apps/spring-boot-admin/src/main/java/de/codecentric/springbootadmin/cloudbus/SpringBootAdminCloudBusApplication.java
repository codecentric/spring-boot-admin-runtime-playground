package de.codecentric.springbootadmin.cloudbus;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class SpringBootAdminCloudBusApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdminCloudBusApplication.class, args);
	}

}
