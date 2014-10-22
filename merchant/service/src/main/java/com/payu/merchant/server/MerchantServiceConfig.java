package com.payu.merchant.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.payu.discovery.register.config.DiscoveryServiceConfig;



@ComponentScan(basePackages = {"com.payu.merchant.server", "com.payu.training.service"})
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:propertasy.properties")
@Import(DiscoveryServiceConfig.class)
public class MerchantServiceConfig {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MerchantServiceConfig.class, args);
    }

}
