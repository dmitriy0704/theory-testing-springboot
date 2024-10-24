package dev.folomkin.sandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SandboxApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SandboxApplication.class);
        application.addListeners(new SwaggerConfiguration());
        application.run(args);
    }
}
