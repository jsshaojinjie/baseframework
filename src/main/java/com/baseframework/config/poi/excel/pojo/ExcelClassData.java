package com.baseframework.config.poi.excel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcelClassData<T> {
    private Class<T> tClassList;
    private Integer titleRowNum;
}
