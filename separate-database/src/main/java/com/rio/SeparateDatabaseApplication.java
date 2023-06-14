package com.rio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SeparateDatabaseApplication {

  public static void main(String[] args) {
    SpringApplication.run(SeparateDatabaseApplication.class, args);
  }
}
