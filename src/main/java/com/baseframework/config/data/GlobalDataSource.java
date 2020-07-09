package com.baseframework.config.data;

import io.lettuce.core.dynamic.annotation.Value;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.datasource")
@Component
@Data
public class GlobalDataSource {
    private String username;
    private String password;
    private String driverClassName;
    private String url;
}
