package com.baseframework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author 邵锦杰
 */
@SpringBootApplication
@EnableCaching
public class BaseFrameworkApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseFrameworkApplication.class, args);
    }

}
