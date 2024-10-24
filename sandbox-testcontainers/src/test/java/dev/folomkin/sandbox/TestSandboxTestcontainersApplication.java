package dev.folomkin.sandbox;

import org.springframework.boot.SpringApplication;

public class TestSandboxTestcontainersApplication {

    public static void main(String[] args) {
        SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
