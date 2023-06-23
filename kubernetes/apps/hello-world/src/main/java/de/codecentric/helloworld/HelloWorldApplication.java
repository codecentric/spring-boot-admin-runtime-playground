package de.codecentric.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class HelloWorldApplication {

  public static void main(String... args) {
    var app = new SpringApplication(HelloWorldApplication.class);
    // Buffering for Startup-Actuator
    app.setApplicationStartup(new BufferingApplicationStartup(2048));
    app.run();
  }

}
