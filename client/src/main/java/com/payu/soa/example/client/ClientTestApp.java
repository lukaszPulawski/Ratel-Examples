package com.payu.soa.example.client;

import com.payu.discovery.event.EventDrivenConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = "com.payu.soa.example")
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:client.properties")
@Import(EventDrivenConfig.class)
public class ClientTestApp {

    public static void main(String[] args) {
        SpringApplication.run(ClientTestApp.class, args);
    }

}
