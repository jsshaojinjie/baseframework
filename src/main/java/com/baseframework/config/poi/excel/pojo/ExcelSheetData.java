package com.baseframework.config.poi.excel.pojo;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邵锦杰
 * @time 2019/3/20
 * @description ${description}
 */
public class ExcelSheetData<T> {
    private int sheetNum;
    private Sheet sheet;
    private List<ExcelRowData<T>> rowDataList;
    private List<String> headErrMsgList = new ArrayList<>();

    public ExcelSheetData(int sheetNum, Sheet sheet) {
        this.sheetNum = sheetNum;
        this.sheet = sheet;
    }

    public int getSheetNum() {
        return sheetNum;
    }

    public void setSheetNum(int sheetNum) {
        this.sheetNum = sheetNum;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public List<ExcelRowData<T>> getRowDataList() {
        return rowDataList;
    }

    public void setRowDataList(List<ExcelRowData<T>> rowDataList) {
        this.rowDataList = rowDataList;
    }

    public List<String> getHeadErrMsgList() {
        return headErrMsgList;
    }

    public void setHeadErrMsgList(List<String> headErrMsgList) {
        this.headErrMsgList = headErrMsgList;
    }
}
