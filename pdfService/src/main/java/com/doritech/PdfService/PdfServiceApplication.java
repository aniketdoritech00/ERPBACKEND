package com.doritech.PdfService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.doritech.PdfService.FeignClient")
public class PdfServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PdfServiceApplication.class, args);
    }
}