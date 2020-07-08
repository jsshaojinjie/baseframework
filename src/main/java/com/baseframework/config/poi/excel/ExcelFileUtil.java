package com.baseframework.config.poi.excel;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ExcelFileUtil {


    public String save(String filename, InputStream inputStream) {
        String filePath =  "";
        return filePath;
    }


    public InputStream get(String bucketName, String filename) {
        return null;
    }

    public InputStream get(String filePath) {
        return null;
    }
}
