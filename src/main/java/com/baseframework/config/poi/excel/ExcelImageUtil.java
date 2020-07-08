package com.baseframework.config.poi.excel;

import cn.hutool.core.util.IdUtil;
import com.baseframework.config.poi.excel.pojo.ExcelImageData;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelImageUtil {

    @Autowired
    private ExcelFileUtil excelFileUtil;

    //获取图片
    public Map<String, List<ExcelImageData>> readImageData(InputStream inputStream, int sheetNum, String filename) throws Exception {
        Map<String, List<ExcelImageData>> result = new HashMap<>();
        if (ExcelVerifyUtil.isExcel03(filename)) {
            result = getPictures(new HSSFWorkbook(inputStream).getSheetAt(sheetNum));
        } else {
            result = getPictures(new XSSFWorkbook(inputStream).getSheetAt(sheetNum));
        }
        return result;
    }

    //03
    protected Map<String, List<ExcelImageData>> getPictures(HSSFSheet sheet) throws Exception {
        Map<String, List<ExcelImageData>> map = new HashMap<String, List<ExcelImageData>>();
        if (sheet.getDrawingPatriarch() == null) return map;
        List<HSSFShape> list = sheet.getDrawingPatriarch().getChildren();
        for (HSSFShape shape : list) {
            if (shape instanceof HSSFPicture) {
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor cAnchor = picture.getClientAnchor();
                HSSFPictureData pdata = picture.getPictureData();
                //存储图片
                String suffix = "";
                switch (pdata.suggestFileExtension()) {
                    case "jpeg":
                        suffix = "jpg";
                        break;
                    default:
                        suffix = pdata.suggestFileExtension();
                }
                String filename = IdUtil.simpleUUID() + "." + suffix;
                String filePath = excelFileUtil.save(filename, new ByteArrayInputStream(pdata.getData()));
                String key = cAnchor.getRow1() + "_" + cAnchor.getCol1(); // 行号-列号
                if (map.containsKey(key)) {
                    map.get(key).add(new ExcelImageData(filePath, pdata.getData().length));
                } else {
                    List<ExcelImageData> excelImageData = new ArrayList<>();
                    excelImageData.add(new ExcelImageData(filePath, pdata.getData().length));
                    map.put(key, excelImageData);
                }
            }
        }
        return map;
    }

    //07
    protected Map<String, List<ExcelImageData>> getPictures(XSSFSheet sheet) throws Exception {
        Map<String, List<ExcelImageData>> map = new HashMap<String, List<ExcelImageData>>();
        List<POIXMLDocumentPart> list = sheet.getRelations();
        for (POIXMLDocumentPart part : list) {
            if (part instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) part;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    //存储图片
                    String suffix = "";
                    switch (picture.getPictureData().suggestFileExtension()) {
                        case "jpeg":
                            suffix = "jpg";
                            break;
                        default:
                            suffix = picture.getPictureData().suggestFileExtension();
                    }
                    String filename = IdUtil.simpleUUID() + "." + suffix;
                    String filePath = excelFileUtil.save(filename, new ByteArrayInputStream(picture.getPictureData().getData()));
                    XSSFClientAnchor anchor = picture.getPreferredSize();
                    CTMarker marker = anchor.getFrom();
                    String key = marker.getRow() + "_" + marker.getCol();

                    if (map.containsKey(key)) {
                        map.get(key).add(new ExcelImageData(filePath, picture.getPictureData().getData().length));
                    } else {
                        List<ExcelImageData> excelImageData = new ArrayList<>();
                        excelImageData.add(new ExcelImageData(filePath, picture.getPictureData().getData().length));
                        map.put(key, excelImageData);
                    }
                }
            }
        }
        return map;
    }
}
