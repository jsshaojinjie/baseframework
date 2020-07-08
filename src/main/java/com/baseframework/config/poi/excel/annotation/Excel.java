package com.baseframework.config.poi.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 邵锦杰
 * @time 2019/3/20
 * @description ${description}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {
    //name = "学生姓名"
    String name();

    //必须为string 转string 或者 int 类型的 replace = {"男_1","女_2"}
    String[] replace() default {};

    //是否必填
    boolean require() default false;

    //图片类型
    boolean imageType() default false;

    int imageCount() default 5;

    //必须为指定长度
    int strLength() default 0;

    //必须小于指定长度
    int strMaxLength() default 0;

    //必须大于指定长度
    int strMinLength() default 0;

    //必须小于等于此长度
    int intMax() default Integer.MAX_VALUE;

    //必须大于等于此长度
    int intMin() default Integer.MIN_VALUE;

    float floatMax() default Float.MAX_VALUE;

    float floatMin() default Float.MIN_VALUE;

    double doubleMax() default Double.MAX_VALUE;

    double doubleMin() default Double.MIN_VALUE;
}
