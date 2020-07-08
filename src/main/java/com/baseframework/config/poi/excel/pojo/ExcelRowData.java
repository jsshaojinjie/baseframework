package com.baseframework.config.poi.excel.pojo;

import java.util.List;

/**
 * @author 邵锦杰
 * @time 2019/3/20
 * @description ${description}
 */
public class ExcelRowData<T> {
    private Integer row;
    private T data;
    private Integer lastRow;
    private List<ExcelRowColData> rowColDataList;


    public ExcelRowData(Integer row, Integer lastRow) {
        this.row = row;
        this.lastRow = lastRow;
    }

    public Boolean success() {
        for (ExcelRowColData excelRowColData : rowColDataList) {
            if (excelRowColData.getErrMsgList().size() > 0) {
                return false;
            }
        }
        return true;
    }


    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getLastRow() {
        return lastRow;
    }

    public void setLastRow(Integer lastRow) {
        this.lastRow = lastRow;
    }

    public List<ExcelRowColData> getRowColDataList() {
        return rowColDataList;
    }

    public void setRowColDataList(List<ExcelRowColData> rowColDataList) {
        this.rowColDataList = rowColDataList;
    }
}
