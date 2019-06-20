package com.banfftech.platformmanager.util;


import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;



public class ExportExcelFile {

    /**
     * @param request
     * @param response
     * @param fileTitel 表头的数组 String类型
     * @param content   表的数据集，列顺序和表头title的顺序一致
     * @param fileName  需要导出的文件名
     */
    public static void exportExcel(HttpServletRequest request, HttpServletResponse response, String[] fileTitel,
                                   List<Map<String, String>> content, String fileName) {
        HSSFWorkbook wb = new HSSFWorkbook(); // --->创建了一个excel文件
        HSSFSheet sheet = wb.createSheet(fileName); // --->创建了一个工作簿
        HSSFRow row = sheet.createRow(0); // --->创建一行
        // 第一行循环写入表头
        for (int i = 0; i < fileTitel.length; i++) {
            row.createCell(i).setCellValue(fileTitel[i]);
        }

        int i = 1;
        // 传入的数据集循环，新增行
        for (Map<String, String> tempMap : content) {
            HSSFRow nRow = sheet.createRow(i);
            for (int j = 0; j < fileTitel.length; j++) {
                nRow.createCell(j).setCellValue(tempMap.get(j + ""));
            }
            i++;
        }

        fileName += ".xls";
        try {
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param response
     * @param fileTitel 表头的数组 String类型
     * @param content   表的数据集，列顺序和表头title的顺序一致
     * @param fileName  需要导出的文件名
     */
    public static void exportExcelArray(HttpServletRequest request, HttpServletResponse response, String[] fileTitel,
                                        List<String[]> content, String fileName) {
        XSSFWorkbook wb = new XSSFWorkbook(); // --->创建了一个excel文件
        XSSFSheet sheet = wb.createSheet(fileName); // --->创建了一个工作簿
        XSSFRow row = sheet.createRow(0); // --->创建一行
        // 第一行循环写入表头
        for (int i = 0; i < fileTitel.length; i++) {
            row.createCell(i).setCellValue(fileTitel[i]);
        }

        int i = 1;
        // 传入的数据集循环，新增行
        for (String[] arr : content) {
            XSSFRow nRow = sheet.createRow(i);
            for (int j = 0; j < arr.length; j++) {
                nRow.createCell(j).setCellValue(arr[j] + "");
            }
            i++;
        }

        fileName += ".xlsx";
        try {
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
            wb.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param response
     * @param fileTitel 表头的数组 String类型
     * @param content   表的数据集，列顺序和表头title的顺序一致
     * @param fileName  需要导出的文件名
     */
    public static void exportExcelMap(HttpServletRequest request, HttpServletResponse response,
                                      List<Map<String, Object>> content, String[] fileTitel, String mapKeys, String fileName) {
        XSSFWorkbook wb = new XSSFWorkbook(); // --->创建了一个excel文件
        XSSFSheet sheet = wb.createSheet(fileName); // --->创建了一个工作簿
        XSSFRow row = sheet.createRow(0); // --->创建一行
        String[] keys = mapKeys.split(",");
        // 第一行循环写入表头
        for (int i = 0; i < fileTitel.length; i++) {
            row.createCell(i).setCellValue(fileTitel[i]);
        }

        int i = 1;
        // 传入的数据集循环，新增行
        for (Map<String, Object> map : content) {
            XSSFRow nRow = sheet.createRow(i);
            for (int j = 0; j < keys.length; j++) {
                if (map.containsKey(keys[j])) {
                    Object obj = map.get(keys[j]);
                    String str = "";
                    if (UtilValidate.isEmpty(obj)) {
                        nRow.createCell(j).setCellValue("");
                    } else {
                        if (obj.getClass().getSimpleName().equals("Integer")) {
                            nRow.createCell(j).setCellValue((Integer) obj);
                        } else if (obj.getClass().getSimpleName().equals("Double")) {
                            nRow.createCell(j).setCellValue((Double) obj);
                        } else if (obj.getClass().getSimpleName().equals("BigDecimal")) {
                            nRow.createCell(j).setCellValue(((BigDecimal) obj).doubleValue());
                        } else {
                            nRow.createCell(j).setCellValue(obj.toString());
                        }

                    }
                }
            }

            i++;
        }

        fileName += ".xlsx";
        try {
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
