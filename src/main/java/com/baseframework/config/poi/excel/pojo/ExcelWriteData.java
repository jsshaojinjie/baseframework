package com.baseframework.config.poi.excel.pojo;


import java.util.List;

/**
 * @author 邵锦杰
 * @time 2019/3/26
 * @description ${description}
 */
public class ExcelWriteData<T> {
    private List<T> dataList;
    private Class<T> clazz;


    public ExcelWriteData(List<T> dataList, Class<T> clazz) {
        this.dataList = dataList;
        this.clazz = clazz;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
}
