package com.example.springtest;

import org.springframework.boot.SpringApplication;


public class TestSptingTestApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringTestApplication::main).run(args);
    }

}
