package edu.example.demoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"edu.example.demoproject"})
public class DemoProjectApplication {

  public static void main(String[] args) {

    SpringApplication.run(DemoProjectApplication.class, args);
  }

}
