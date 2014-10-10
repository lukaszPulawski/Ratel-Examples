package com.payu.soa.example.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = "com.payu.soa")
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:client.properties")
public class ClientTestApp {

    @Autowired
    private OrderServiceProducer asyncOrderService;

    public static void main(String[] args) {
        SpringApplication.run(ClientTestApp.class, args);
    }

}
