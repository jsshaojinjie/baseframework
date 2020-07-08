package com.baseframework.config.poi.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 邵锦杰
 * @time 2019/3/27
 * @description ${description}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelTable {
    //name = "导出时sheet名"
    String sheetName();
    //每个sheet最多
    int sheetRowMaxCount() default 60000;
}
