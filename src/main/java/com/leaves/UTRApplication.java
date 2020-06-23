package com.leaves;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan//扫描@WebFilter、@WebServlet等组件
public class UTRApplication {

    public static void main(String[] args) {
        SpringApplication.run(UTRApplication.class, args);
    }
}
