package com.baseframework.config.poi.excel.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邵锦杰
 * @time 2019/3/20
 * @description ${description}
 */
public class ExcelRowColData {
    private Integer row;
    private Integer col;
    private Object data;
    //对象属性名称
    private String fieldName;
    //excel表格中的中文名称
    private String excelName;
    private List<String> errMsgList = new ArrayList<>();


    public ExcelRowColData(Integer row, Integer col, Object data, String fieldName,String excelName) {
        this.row = row;
        this.col = col;
        this.data = data;
        this.fieldName = fieldName;
        this.excelName = excelName;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<String> getErrMsgList() {
        return errMsgList;
    }

    public void setErrMsgList(List<String> errMsgList) {
        this.errMsgList = errMsgList;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }
}
