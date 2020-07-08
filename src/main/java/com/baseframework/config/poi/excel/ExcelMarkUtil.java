package com.baseframework.config.poi.excel;

import cn.hutool.core.util.StrUtil;
import com.baseframework.config.poi.excel.pojo.ExcelResult;
import com.baseframework.config.poi.excel.pojo.ExcelRowColData;
import com.baseframework.config.poi.excel.pojo.ExcelRowData;
import com.baseframework.config.poi.excel.pojo.ExcelSheetData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 邵锦杰
 * @time 2019/3/26
 * @description ${description}
 */
@Service
public class ExcelMarkUtil {
    @Autowired
    private ExcelFileUtil excelFileUtil;
    private static String bucketName = "syseximport";
//    private static String bucketName = CommonConstants.BUCKET_NAME;

    //标记
    public <T> String mark(ExcelResult<T> result) {
        return this.mark(result, null);
    }


    //标记  + 对应的模块名称
    public <T> String mark(ExcelResult<T> result, String moduleName) {
        try {
            Workbook workbook = result.getWorkbook();
            CellStyle cellStyle = ExcelStyleUtil.getErrorCellStyle(workbook);
            for (Integer i = 0; i < result.getDataList().size(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                ExcelSheetData<T> excelSheetData = result.getDataList().get(i);

                //以标题最后一列为准，不再以每行最后一列
                int lastCol = sheet.getRow(0).getLastCellNum();
                if (excelSheetData.getHeadErrMsgList().size() > 0) {
                    Row row = sheet.getRow(0);
                    Cell cell = row.createCell(lastCol);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(StringUtils.join(excelSheetData.getHeadErrMsgList(), ";"));
                }

                for (ExcelRowData<T> excelRowData : excelSheetData.getRowDataList()) {
                    List<String> errMsgList = new ArrayList<>();
                    for (ExcelRowColData excelRowColData : excelRowData.getRowColDataList()) {
                        if (excelRowColData.getErrMsgList().size() > 0) {
                            Row row = sheet.getRow(excelRowColData.getRow());
                            Cell cell = row.getCell(excelRowColData.getCol());
                            if (cell == null) {
                                cell = row.createCell(excelRowColData.getCol());
                            }
                            cell.setCellStyle(cellStyle);
                            String errMsg = StringUtils.join(excelRowColData.getErrMsgList(), ",");
                            errMsgList.add(errMsg);
                        }
                    }
                    //每一行最后添加所在行所有错误
                    if (errMsgList.size() > 0) {
                        String errMsg = StringUtils.join(errMsgList, ";");
                        Row row = sheet.getRow(excelRowData.getRow());
                        Cell cell = row.createCell(lastCol);
                        cell.setCellValue(errMsg);
                    }
                }
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            outputStream.close();
            String fileName = "";
            if (StrUtil.isEmpty(moduleName)) {
                fileName = "错误提示文件.xls";
            } else {
                fileName = moduleName + "错误提示文件.xls";
            }
            String path = excelFileUtil.save(fileName, inputStream);
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
