package com.act.HR_management.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InitServices{
    private final TaxRateRuleService taxRateRuleService;
    private final DeparmentService deparmentService;
    private final EmployeeService employeeService;


    @Bean
   public CommandLineRunner onInit() {
        return args -> {
            this.taxRateRuleService.onInit();
            this.deparmentService.onInit();
            this.employeeService.onInit();
        };
    }


}
