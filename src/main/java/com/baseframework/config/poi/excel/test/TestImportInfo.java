package com.baseframework.config.poi.excel.test;


import com.baseframework.config.poi.excel.annotation.Excel;
import com.baseframework.config.poi.excel.annotation.ExcelTable;
import lombok.Data;

@Data
@ExcelTable(sheetName = "测试")
public class TestImportInfo {
    @Excel(name = "序号")
    private Integer id;
    @Excel(name = "名称", require = true)
    private String name;
}
