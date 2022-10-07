package com.yil.workflow;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = {"com.yil"})
@OpenAPIDefinition(info = @Info(title = "Workflow Api", version = "1.0", description = "Yıldırım Information"))
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class WorkflowApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WorkflowApplication.class, args);
        context.start();
    }
}
