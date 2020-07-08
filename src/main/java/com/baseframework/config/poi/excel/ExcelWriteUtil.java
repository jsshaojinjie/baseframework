package com.baseframework.config.poi.excel;


import cn.hutool.core.util.StrUtil;
import com.baseframework.config.poi.excel.annotation.ExcelTable;
import com.baseframework.config.poi.excel.pojo.ExcelWriteData;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.baseframework.config.poi.excel.ExcelUtil.*;

/**
 * @author 邵锦杰
 * @time 2019/3/26
 * @description ${description}
 */

@Service
public class ExcelWriteUtil {

    private static int rowMaxCount = 60000;

    @Autowired
    private ExcelFileUtil excelFileUtil;

    public String writeExcel( String fileName, ExcelWriteData... dataList) {
        List<Workbook> workbookList = new ArrayList<Workbook>();
        for (int i = 0; i < dataList.length; i++) {
            //文件索引，多文件
            int index = 0;
            ExcelWriteData excelWriteData = dataList[i];
            ExcelTable excelTable = (ExcelTable) excelWriteData.getClazz().getAnnotation(ExcelTable.class);
            //每个sheet只显示sheetRowMaxCount行数据
            int sheetRowMaxCount = (excelTable.sheetRowMaxCount() > rowMaxCount || excelTable.sheetRowMaxCount() < 1) ? rowMaxCount : excelTable.sheetRowMaxCount();
            //获取每个sheet的image的大小
            long perSheetImageByteSize = getPerSheetImageByteSize(dataList);
            //单个sheet的数据行索引
            Integer startIndex = 0;
            do {
                Workbook workbook;
                if (workbookList.size() > index) {
                    workbook = workbookList.get(index);
                } else {
                    workbook = new HSSFWorkbook();
                    workbookList.add(workbook);
                }
                Sheet sheet = workbook.createSheet();
                ExcelStyleUtil.setCommonDataSheetStyle(sheet);
                if (excelTable != null && excelTable.sheetName() != null) {
                    //每次都是创建新的sheet，所以需要命名的sheet都是workbook最后一个sheet
                    workbook.setSheetName(workbook.getNumberOfSheets() - 1, excelTable.sheetName());
                }
                writeExcelTitle(workbook, sheet, excelWriteData.getClazz());
                startIndex = writeExcelData(workbook, sheet, excelWriteData, startIndex, sheetRowMaxCount, perSheetImageByteSize, excelWriteData.getClazz());
                index++;
            } while (startIndex != null);
        }
        List<ByteArrayInputStream> inputStreamList = new ArrayList<>();
        //数据写入到文件中
        for (int i = 0; i < workbookList.size(); i++) {
            Workbook workbook = workbookList.get(i);
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                inputStreamList.add(inputStream);
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String filePath;
        //如果文件是一个，则直接返回文件地址，如果为多个，则进行打包压缩，返回压缩文件地址
        if (inputStreamList.size() == 1) {
            filePath = excelFileUtil.save(fileName + ".xls", inputStreamList.get(0));
        } else {
            HashMap<String, InputStream> map = new HashMap<>();
            for (int i = 0; i < inputStreamList.size(); i++) {
                map.put(fileName + (i + 1) + ".xls", inputStreamList.get(i));
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            toZip(map, outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            filePath = excelFileUtil.save(fileName + ".zip", inputStream);
        }
        return filePath;
    }


    private static long getPerSheetImageByteSize(ExcelWriteData... dataList) {
        return 1024 * 1024 * 100 / dataList.length;
    }


    //插入表头
    private void writeExcelTitle(Workbook workbook, Sheet sheet, Class c) {
        CellStyle cellStyle = ExcelStyleUtil.getCommonTitleCellStyle(workbook);
        List<String> excelNameList = getExcelNameList(c);
        Row row = sheet.createRow(0);
        //冻结第一行
        sheet.createFreezePane(0, 1);
        int col = 0;
        for (String name : excelNameList) {
            Cell cell = row.createCell(col++);
            cell.setCellValue(name);
            cell.setCellStyle(cellStyle);
        }
    }

    //插入数据
    private Integer writeExcelData(Workbook workbook, Sheet sheet, ExcelWriteData data, Integer startIndex, Integer sheetRowMaxCount, long perSheetImageByteSize, Class c) {
        DateTimeFormatter ld = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter ldt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CellStyle dataCellStyle = ExcelStyleUtil.getCommonDataCellStyle(workbook);
        CellStyle imageCellStyle = ExcelStyleUtil.getCommonImageCellStyle(workbook);
        Long imageSize = 0l;
        try {
            List<String> fieldNameList = getFieldNameList(c);
            int rowNum = 1;
            for (int i = startIndex; i < data.getDataList().size(); i++) {
                Object o = data.getDataList().get(i);
                int col = 0;
                Row row = sheet.createRow(rowNum++);
                for (String fieldName : fieldNameList) {
                    Field field = ObjectUtil.getField(c, fieldName);
                    field.setAccessible(true);
                    Object value = field.get(o);
                    Cell cell = row.createCell(col++);
                    //假如是图片字段
                    if (hasImageAnnotation(c, fieldName)) {
                        if (!StringUtils.isEmpty(String.valueOf(value))) {
                            imageSize += writeExcelImage(workbook, sheet, rowNum, col - 1, String.valueOf(value));
                            cell.setCellStyle(imageCellStyle);
                        }
                        continue;
                    } else {
                        cell.setCellStyle(dataCellStyle);
                    }
                    //字段映射 replace字段
                    Map<String, String> replaceAttributeMap = getReplaceAttributeMap(c, fieldName, false);
                    if (replaceAttributeMap.size() > 0) {
                        value = replaceAttributeMap.get(String.valueOf(value));
                    }
                    //写入数据
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    } else if (value instanceof Float) {
                        cell.setCellValue((Float) value);
                    } else if (value instanceof LocalDate) {
                        cell.setCellValue(ld.format((LocalDate) value));
                    } else if (value instanceof LocalDateTime) {
                        cell.setCellValue(ldt.format((LocalDateTime) value));
                    } else if (value instanceof Long) {
                        cell.setCellValue((Long) value);
                    }
                }
                //每个sheet只显示sheetRowMaxCount行数据，返回还没有写入的数据的第一个数据索引
                if (!((i - startIndex) < (sheetRowMaxCount - 1)) && i != data.getDataList().size() - 1) {
                    return i + 1;
                }
                //如果sheet中的图片大小超过指定大小，则返回
                if (imageSize > perSheetImageByteSize) {
                    return i + 1;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    //插入图片
    //返回插入图片的大小
    private Long writeExcelImage(Workbook workbook, Sheet sheet, int row, int col, String imagePaths) {
        Long size = 0l;
        String[] imagePathsSplit = imagePaths.split(",");
        for (int t = 0; t < imagePathsSplit.length; t++) {
            if (imagePathsSplit[t].split(StrUtil.DASHED).length != 2) continue;
            String filePath = imagePathsSplit[t].split(StrUtil.DASHED)[1];
            String fileBucketName = imagePathsSplit[t].split(StrUtil.DASHED)[0];
            try {
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                //TODO 计算图片大小
                //size+=
                BufferedImage bufferImg = ImageIO.read(excelFileUtil.get(fileBucketName, filePath));
                HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
                // anchor主要用于设置图片的属性
                HSSFClientAnchor anchor = new HSSFClientAnchor(200 * t + 20, 0 + 20, 200 * t + 200, 220, (short) col, row - 1, (short) col, row - 1);
                // 插入图片
                int index = 0;
                if (filePath.contains(".png")) {
                    ImageIO.write(bufferImg, "png", byteArrayOut);
                    index = workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
                } else if (filePath.contains(".jpg")) {
                    ImageIO.write(bufferImg, "jpg", byteArrayOut);
                    index = workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG);
                }
                Picture pict = patriarch.createPicture(anchor, index);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return size;
    }

    private static final int BUFFER_SIZE = 2 * 1024;

    public static void toZip(Map<String, InputStream> inputStreamMap, OutputStream out) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (String filename : inputStreamMap.keySet()) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(filename));
                int len;
                InputStream in = inputStreamMap.get(filename);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
