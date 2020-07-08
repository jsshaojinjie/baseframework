package com.baseframework.config.poi.excel.pojo;

import com.sun.deploy.util.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邵锦杰
 * @time 2019/3/20
 * @description ${description}
 */
public class ExcelResult<T> {
    private List<ExcelSheetData<T>> dataList = new ArrayList<>();
    private Workbook workbook;
    //此处主要是解析是否正确的标志
    private Boolean readSuccess = true;
    //总的提示信息
    private String readErrorMsg;


    public Boolean success() {
        for (ExcelSheetData<T> excelSheetData : dataList) {
            if (excelSheetData.getHeadErrMsgList().size() > 0) {
                return false;
            }
            for (ExcelRowData<T> excelRowData : excelSheetData.getRowDataList()) {
                for (ExcelRowColData excelRowColData : excelRowData.getRowColDataList()) {
                    if (excelRowColData.getErrMsgList().size() > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String errMsg() {
        List<String> stringList = new ArrayList<>();
        for (ExcelSheetData<T> excelSheetData : dataList) {

            if (excelSheetData.getHeadErrMsgList().size() > 0) {
                stringList.add(excelSheetData.getSheet().getSheetName() + "页" + StringUtils.join(excelSheetData.getHeadErrMsgList(), "；"));
            }

            int num = 0;
            for (ExcelRowData<T> excelRowData : excelSheetData.getRowDataList()) {
                for (ExcelRowColData excelRowColData : excelRowData.getRowColDataList()) {
                    if (excelRowColData.getErrMsgList().size() > 0) {
                        num++;
                        break;
                    }
                }
            }
            if (num > 0) {
                stringList.add(excelSheetData.getSheet().getSheetName() + "页有" + num + "条数据错误");
            }

        }
        if (stringList.size() > 0) {
            return StringUtils.join(stringList, "；") + "。";
        } else {
            return "导入文件模板可能存在问题，请使用正确导入模板";
        }

    }

    public ExcelResult(Workbook workbook) {
        this.workbook = workbook;
    }


    public ExcelResult() {
    }

    public List<ExcelSheetData<T>> getDataList() {
        return dataList;
    }

    public void setDataList(List<ExcelSheetData<T>> dataList) {
        this.dataList = dataList;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public Boolean getReadSuccess() {
        return readSuccess;
    }

    public void setReadSuccess(Boolean readSuccess) {
        this.readSuccess = readSuccess;
    }

    public String getReadErrorMsg() {
        return readErrorMsg;
    }

    public void setReadErrorMsg(String readErrorMsg) {
        this.readErrorMsg = readErrorMsg;
    }
}
