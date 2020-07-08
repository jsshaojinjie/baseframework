package com.baseframework.config.poi.excel;

import cn.hutool.core.util.StrUtil;
import com.baseframework.config.poi.excel.annotation.Excel;
import com.baseframework.config.poi.excel.exception.ExcelImportException;
import com.baseframework.config.poi.excel.pojo.*;
import com.sun.deploy.util.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baseframework.config.poi.excel.ExcelUtil.*;


/**
 * @author 邵锦杰
 * @time 2019/3/20
 * @description ${description}
 */
@Service
public class ExcelReadUtil {

    @Autowired
    private ExcelFileUtil excelFileUtil;

    @Autowired
    private ExcelImageUtil excelImageUtil;




    /**
     * 标准excel读取，标题为第一行，有图片
     */
    public ExcelResult read(String filePath, Class... tClassList) {
        ExcelClassData[] list = new ExcelClassData[tClassList.length];
        for (int i = 0; i < tClassList.length; i++) {
            list[i] = new ExcelClassData(tClassList[i], 1);
        }
        return this.readExcelData(filePath, list);
    }

    /**
     * 非标准excel读取，标题行自定义，有图片
     */
    public ExcelResult read(String filePath, ExcelClassData... excelClassDataList) {
        return this.readExcelData(filePath, excelClassDataList);
    }


    //已弃用，请使用read方法
    @Deprecated
    public ExcelResult readExcelData(String filePath, Class... tClassList) {
        ExcelClassData[] list = new ExcelClassData[tClassList.length];
        for (int i = 0; i < tClassList.length; i++) {
            list[i] = new ExcelClassData(tClassList[i], 1);
        }
        return this.readExcelData(filePath, list);
    }

    /**
     * 读取excel文件
     * @param filePath           文件地址
     * @param excelClassDataList 对象的基本信息
     */
    private ExcelResult readExcelData(String filePath, ExcelClassData... excelClassDataList) {
        ExcelResult excelResult = new ExcelResult();
        try {
            Workbook workbook = getExcelWorkbook(filePath, excelFileUtil.get(filePath));
            if (workbook.getNumberOfSheets() < excelClassDataList.length) {
                throw new ExcelImportException("excel中sheet数据页过少，请使用正确模板");
            }
            excelResult.setWorkbook(workbook);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                //图片数据
                Map<String, List<ExcelImageData>> imageDataMap = new HashMap<>();
                imageDataMap = excelImageUtil.readImageData( excelFileUtil.get(filePath), i, filePath);
                excelResult.getDataList().add(readExcelData(workbook, excelClassDataList[i], i, imageDataMap));
            }
            return excelResult;
        } catch (ExcelImportException e) {
            excelResult.setReadSuccess(false);
            excelResult.setReadErrorMsg("导入文件模板可能存在问题，请使用正确导入模板");
        } catch (Exception e) {
            excelResult.setReadSuccess(false);
            excelResult.setReadErrorMsg("导入文件模板可能存在问题，请使用正确导入模板");
            e.printStackTrace();
        }
        return excelResult;
    }


    //读取Excel数据，将相对应的数据根据第一排列名复制到对象的相应属性;
    private <T> ExcelSheetData<T> readExcelData(Workbook workbook, ExcelClassData<T> excelClassData, int sheetNum, Map<String, List<ExcelImageData>> imageDataMap) throws IllegalAccessException, InstantiationException {

        Class<T> tClass = excelClassData.getTClassList();
        int titleRowNum = excelClassData.getTitleRowNum() - 1;
        //获取fieldname 与 中文名的映射
        Map<String, String> excelNameMapFieldName = getFieldNameMapExcelName(tClass, false);
        if (workbook.getNumberOfSheets() < (sheetNum + 1)) {
            return null;
        }
        Sheet sheet = workbook.getSheetAt(sheetNum);
        //sheet数据
        ExcelSheetData<T> excelSheetData = new ExcelSheetData(sheetNum, sheet);
        //列号与title绑定
        HashMap<Integer, String> titleCellNum = new HashMap<Integer, String>();
        //行数据集
        List<ExcelRowData<T>> rowDataList = new ArrayList<>();
        //每一行
        for (int j = titleRowNum; j < sheet.getLastRowNum() + 1; j++) { // getLastRowNum，获取最后一行的行标
            T obj = tClass.newInstance();
            Row row = sheet.getRow(j);
            if (isBlankRow(row)) {
                continue;
            }
            ExcelRowData<T> excelRowData = null;
            if (j > titleRowNum) {
                excelRowData = new ExcelRowData(j, (int) row.getLastCellNum());
            }
            List<ExcelRowColData> rowColDataList = new ArrayList<>();
            /**
             * 验证表头
             * 1.必填字段是否存在
             * */
            if (j == titleRowNum) {
                ExcelVerifyUtil.verifyTitle(excelSheetData, tClass, titleRowNum);
            }

            //每一列
            for (int k = 0; k < sheet.getRow(titleRowNum).getLastCellNum(); k++) {
                //第一行，将title与列号进行映射
                if (j == titleRowNum) {
                    String data = String.valueOf(getCellValue(sheet, j, k));
                    //获取标题
                    titleCellNum.put(k, data);
                } else {
                    //数据行并且与实体类有对应的数据
                    if (excelNameMapFieldName.containsKey(titleCellNum.get(k))) {
                        String fieldName = excelNameMapFieldName.get(titleCellNum.get(k));
                        Excel excel = ExcelVerifyUtil.getExcelAnnotation(tClass, fieldName);
                        Object data;
                        //读取数据
                        if (excel.imageType()) {
                            //图片数据是个对象，需要进行转换
                            //TODO 图片合并单元格获取
                            List<String> dataList = new ArrayList<>();
                            List<ExcelImageData> excelImageDataList = imageDataMap.get(j + "_" + k);
                            if (excelImageDataList != null) {
                                for (ExcelImageData excelImageData : excelImageDataList) {
                                    dataList.add(excelImageData.getFilePath());
                                }
                            }
                            data = dataList.size() == 0 ? null : StringUtils.join(dataList, StrUtil.COMMA);
                        } else {
                            data = getCellValue(sheet, j, k);
                        }
                        //验证转换数据
                        ExcelRowColData rowColData = new ExcelRowColData(j, k, data, fieldName, titleCellNum.get(k));


                        //先验证必填
                        if (ExcelVerifyUtil.verifyRequire(rowColData, excel, data, titleCellNum.get(k))) {
                            //如果数据为空，则不需要下面
                            if (!ExcelVerifyUtil.verifyDataEmpty(data)) {

                                //如果为图片数据，要验证图片大小
                                Boolean imageSizeVerify = true;
                                if (excel.imageType()) {
                                    List<ExcelImageData> excelImageDataList = imageDataMap.get(j + "_" + k);
                                    if (excelImageDataList != null) {
                                        for (ExcelImageData excelImageData : excelImageDataList) {
                                            //5K 大小
                                            if (excelImageData.getSize() > 1 * 1024 * 1024 * 7) {
                                                imageSizeVerify = false;
                                            }
                                        }
                                    }
                                }
                                if (!imageSizeVerify) {
                                    rowColData.getErrMsgList().add(titleCellNum.get(k) + "单张图片最大为7M");
                                }
                                //再转换数据
                                ObjectUtil.ConvertResult convertResult = ObjectUtil.setDate(obj, data, fieldName);
                                if (convertResult != null && !convertResult.getSuccess()) {
                                    rowColData.getErrMsgList().add(titleCellNum.get(k) + convertResult.getErrMsg());
                                }
                                //再验证其他
                                if (convertResult != null && convertResult.getSuccess() && convertResult.getData() != null) {
                                    ExcelVerifyUtil.verify(rowColData, excel, convertResult.getData(), titleCellNum.get(k));
                                }
                            }
                        }
                        rowColDataList.add(rowColData);
                    }
                }
            }
            if (j > titleRowNum) {
                excelRowData.setData(obj);
                excelRowData.setRowColDataList(rowColDataList);
                rowDataList.add(excelRowData);
            }
        }
        excelSheetData.setRowDataList(rowDataList);
        return excelSheetData;
    }

}
