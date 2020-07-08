package com.baseframework.config.poi.excel;

import com.baseframework.config.poi.excel.annotation.Excel;
import com.baseframework.config.poi.excel.exception.ExcelImportException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 邵锦杰
 * @time 2018/11/21
 * @description ${description}
 */
public class ExcelUtil {
    // 时间的格式
    private static String format = "yyyy-MM-dd";


    /**
     * 功能:处理单元格中值得类型
     *
     * @param cell
     * @return
     */
    protected static Object getCellValue(Cell cell) {
        Object result = null;
        if (cell != null) {
            //统一转成字符串
            cell.setCellType(CellType.STRING);
            switch (cell.getCellTypeEnum()) {
                case NUMERIC: {
                    result = Double.valueOf(cell.getNumericCellValue());
                    break;
                }
                case STRING: {
                    result = cell.getStringCellValue();
                    break;
                }
                case _NONE: {
                    break;
                }
                case BLANK: {
                    result = "";
                    break;
                }
                case ERROR: {
                    result = Byte.valueOf(cell.getErrorCellValue());
                    break;
                }
                case BOOLEAN: {
                    result = Boolean.valueOf(cell.getBooleanCellValue());
                    break;
                }
                case FORMULA: {
                    result = cell.getCellFormula();
                    break;
                }
            }
        }
        return result;
    }


    protected static Object getCellValue(Sheet sheet, int rowNum, int columnNum) {
        if (isMergedRegion(sheet, rowNum, columnNum)) {
            return getMergedRegionValue(sheet, rowNum, columnNum);
        } else {
            Row row = sheet.getRow(rowNum);
            Cell cell = row.getCell(columnNum);
            return getCellValue(cell);
        }
    }


    //根据excel注解，获取属性名与name映射
    protected static LinkedHashMap<String, String> getFieldNameMapExcelName(Class c, Boolean order) {
        Field[] fields = c.getDeclaredFields();
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (field.isAnnotationPresent(Excel.class)) {
                Excel excel = field.getAnnotation(Excel.class);
                if (order) {
                    result.put(fieldName, excel.name());
                } else {
                    result.put(excel.name(), fieldName);
                }
            }
        }
        return result;
    }

    protected static List<String> getFieldNameList(Class c) {
        Field[] fields = c.getDeclaredFields();
        List<String> result = new ArrayList<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (field.isAnnotationPresent(Excel.class)) {
                result.add(fieldName);
            }
        }
        return result;
    }

    protected static List<String> getExcelNameList(Class c) {
        Field[] fields = c.getDeclaredFields();
        List<String> result = new ArrayList<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (field.isAnnotationPresent(Excel.class)) {
                Excel excel = field.getAnnotation(Excel.class);
                result.add(excel.name());
            }
        }
        return result;
    }

    protected static Boolean isBlankRow(Row row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }


    //根据excel注解，获取属性名与name映射
    protected static Boolean hasImageAnnotation(Class c, String fieldName) {
        if (fieldName == null) {
            return false;
        }
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (fieldName.equals(field.getName()) && field.isAnnotationPresent(Excel.class)) {
                Excel excel = field.getAnnotation(Excel.class);
                if (excel != null && excel.imageType()) {
                    return true;
                }
            }
        }
        return false;
    }

    //根据excel注解，获取属性名与name映射
    protected static Map<String, String> getReplaceAttributeMap(Class c, String fieldName, Boolean order) {
        if (fieldName == null) {
            return null;
        }
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (fieldName.equals(field.getName()) && field.isAnnotationPresent(Excel.class)) {
                Excel excel = field.getAnnotation(Excel.class);
                if (excel != null && excel.replace() != null) {
                    HashMap<String, String> result = new HashMap<>();
                    for (String str : excel.replace()) {
                        if (order) {
                            result.put(str.split("_")[0], str.split("_")[1]);
                        } else {
                            result.put(str.split("_")[1], str.split("_")[0]);
                        }

                    }
                    return result;
                }
            }
        }
        return null;
    }


    protected static Workbook getExcelWorkbook(String filePathFile, InputStream inputStream) throws Exception {
        if (!ExcelVerifyUtil.verifySuffix(filePathFile)) {
            throw new ExcelImportException("只支持excel文件格式");
        }
        if (!inputStream.markSupported()) {
            inputStream = new PushbackInputStream(inputStream, 8);
        }
        Workbook workbook = WorkbookFactory.create(inputStream);
        return workbook;
    }


    /**
     * 获取合并单元格的值
     */
    public static Object getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }
        return null;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }
}
