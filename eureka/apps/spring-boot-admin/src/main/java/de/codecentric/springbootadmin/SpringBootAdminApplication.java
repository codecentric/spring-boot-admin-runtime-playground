package de.codecentric.springbootadmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
@EnableAdminServer
public class SpringBootAdminApplication {

  public static void main(String... args) {
    var app = new SpringApplication(SpringBootAdminApplication.class);
    // Buffering for Startup-Actuator
    app.setApplicationStartup(new BufferingApplicationStartup(2048));
    app.run();
  }
}
