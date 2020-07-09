
package com.baseframework.config.data.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author yaomajor
 * @date 2018/10/29
 */
@Configuration
@ConditionalOnBean(DataSource.class)
@MapperScan("com.baseframework.mapper")
public class MybatisPlusConfig {

}
