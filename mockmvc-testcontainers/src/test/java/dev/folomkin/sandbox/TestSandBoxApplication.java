package dev.folomkin.sandbox;

import org.springframework.boot.SpringApplication;

public class TestSandBoxApplication {

    public static void main(String[] args) {
        SpringApplication.from(Application::main)
                .with(TestBeans.class)
                .run(args);
    }
}
