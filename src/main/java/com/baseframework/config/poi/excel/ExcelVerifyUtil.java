package com.baseframework.config.poi.excel;


import cn.hutool.core.util.StrUtil;
import com.baseframework.config.poi.excel.annotation.Excel;
import com.baseframework.config.poi.excel.pojo.ExcelRowColData;
import com.baseframework.config.poi.excel.pojo.ExcelSheetData;
import com.sun.deploy.util.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baseframework.config.poi.excel.ExcelUtil.getCellValue;
import static com.baseframework.config.poi.excel.ExcelUtil.getFieldNameMapExcelName;

/**
 * @author 邵锦杰
 * @time 2019/3/25
 * @description ${description}
 */
public class ExcelVerifyUtil {

    //excel版本判断
    protected static Boolean isExcel03(String filename) {
        String suffix = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        if ((suffix.equals(".xls"))) {
            return true;
        } else if ((suffix.equals(".xlsx"))) {
            return false;
        }
        return null;
    }


    //验证后缀
    protected static Boolean verifySuffix(String filename) {
        String suffix = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        if (!(suffix.equals(".xlsx") || suffix.equals(".xls"))) {
            return false;
        }
        return true;
    }

    //获取注解
    protected static Excel getExcelAnnotation(Class c, String fieldName) {
        Field[] fields = c.getDeclaredFields();
        HashMap<String, String> result = new HashMap<>();
        for (Field field : fields) {
            if (field.getName().equals(fieldName) && field.isAnnotationPresent(Excel.class)) {
                Excel excel = field.getAnnotation(Excel.class);
                return excel;
            }
        }
        return null;
    }

    //验证是否必须含有此列
    protected static Boolean verifyRequire(ExcelRowColData rowColData, Excel excel, Object data, String name) {
        Boolean verify = true;
        if (excel.require() && (data == null || (data instanceof String && "".equals(String.valueOf(data))))) {
            rowColData.getErrMsgList().add(name + "必填");
            verify = false;
        }
        return verify;
    }

    //验证表头
    protected static <T> void verifyTitle(ExcelSheetData<T> excelSheetData, Class tClass, int rowNum) {
        Map<String, String> excelNameMapFieldName = getFieldNameMapExcelName(tClass, false);
        Map<String, String> fieldNameMapExcelName = getFieldNameMapExcelName(tClass, true);
        List<String> ownerFieldNameList = new ArrayList<>();
        Sheet sheet = excelSheetData.getSheet();
        for (int k = 0; k < sheet.getRow(rowNum).getLastCellNum(); k++) {
            String title = String.valueOf(getCellValue(sheet, rowNum, k));
            String fieldName = excelNameMapFieldName.get(title);
            ownerFieldNameList.add(fieldName);
        }

        List<String> lostRequiredExcelNameList = new ArrayList<>();
        List<String> list = ExcelUtil.getFieldNameList(tClass);
        for (String fieldName : list) {
            Excel excel = ExcelVerifyUtil.getExcelAnnotation(tClass, fieldName);
            if (excel.require() && !ownerFieldNameList.contains(fieldName)) {
                lostRequiredExcelNameList.add(fieldNameMapExcelName.get(fieldName));
            }
        }
        if (lostRequiredExcelNameList.size() > 0) {
            excelSheetData.getHeadErrMsgList().add("缺少" + StringUtils.join(lostRequiredExcelNameList, StrUtil.COMMA) + "字段");
        }

    }

    //验证是否必须含有此列
    protected static Boolean verifyDataEmpty(Object data) {
        if ((data == null || (data instanceof String && "".equals(String.valueOf(data))))) {
            return true;
        }
        return false;
    }

    //根据注解验证数据
    protected static Boolean verify(ExcelRowColData rowColData, Excel excel, Object data, String name) {
        Boolean verify = true;

        if (excel.imageType()) {
            if (String.valueOf(data).split(",").length > excel.imageCount()) {
                rowColData.getErrMsgList().add(name + "最多为" + excel.imageCount() + "张图片");
                verify = false;
            }
        } else {

            switch (data.getClass().getSimpleName()) {
                case "String": {
                    if (excel.strLength() > 0 && String.valueOf(data).length() != excel.strLength()) {
                        rowColData.getErrMsgList().add(name + "必须为" + excel.strLength() + "个字符长度");
                        verify = false;
                    }
                    if (excel.strMaxLength() > 0 && String.valueOf(data).length() > excel.strMaxLength()) {
                        rowColData.getErrMsgList().add(name + "必须小于" + excel.strMaxLength() + "个字符长度");
                        verify = false;
                    }
                    if (excel.strMinLength() > 0 && String.valueOf(data).length() < excel.strMinLength()) {
                        rowColData.getErrMsgList().add(name + "必须大于" + excel.strMinLength() + "个字符长度");
                        verify = false;
                    }
                    break;
                }
                case "Integer": {
                    if (excel.intMax() < Integer.MAX_VALUE && ((Integer) data) > excel.intMax()) {
                        rowColData.getErrMsgList().add(name + "必须小于等于" + excel.intMax());
                        verify = false;
                    }
                    if (excel.intMin() > Integer.MIN_VALUE && ((Integer) data) < excel.intMin()) {
                        rowColData.getErrMsgList().add(name + "必须大于等于" + excel.intMin());
                        verify = false;
                    }
                    break;
                }
                case "Double": {
                    if (excel.doubleMax() < Double.MAX_VALUE && ((Double) data) > excel.doubleMax()) {
                        rowColData.getErrMsgList().add(name + "必须小于等于" + excel.doubleMax());
                        verify = false;
                    }
                    if (excel.doubleMin() > Double.MIN_VALUE && ((Double) data) < excel.doubleMin()) {
                        rowColData.getErrMsgList().add(name + "必须大于等于" + excel.doubleMin());
                        verify = false;
                    }
                    break;
                }
                case "Float": {
                    if (excel.floatMax() < Float.MAX_VALUE && ((Float) data) > excel.floatMax()) {
                        rowColData.getErrMsgList().add(name + "必须小于等于" + excel.floatMax());
                        verify = false;
                    }
                    if (excel.floatMin() > Float.MIN_VALUE && ((Float) data) < excel.floatMin()) {
                        rowColData.getErrMsgList().add(name + "必须大于等于" + excel.floatMin());
                        verify = false;
                    }
                    break;
                }
            }
        }


        return verify;
    }
}
