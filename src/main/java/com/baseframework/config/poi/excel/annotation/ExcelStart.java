package com.baseframework.config.poi.excel.annotation;


import com.baseframework.config.poi.excel.*;
import org.springframework.context.annotation.Import;

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
@Import({ExcelWriteUtil.class, ExcelReadUtil.class, ExcelMarkUtil.class, ExcelFileUtil.class, ExcelImageUtil.class})
public @interface ExcelStart {
}
