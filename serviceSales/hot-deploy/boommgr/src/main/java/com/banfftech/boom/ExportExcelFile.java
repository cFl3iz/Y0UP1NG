package main.java.com.banfftech.boom;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

public class ExportExcelFile {

    public final static String module = ExportExcelFile.class.getName();





    /**
     * 生成Excel到本地
     * @param list
     * @return
     * @throws Exception
     */
        public String createExcelToLocal(ArrayList list) throws Exception {
            String fileName="";
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("学生表一");
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow((int) 0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            //style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue("序号");
            cell = row.createCell((short) 1);
            cell.setCellValue("姓名");
            cell = row.createCell((short) 2);
            cell.setCellValue("住址");
            cell = row.createCell((short) 3);
            cell.setCellValue("班级");

            cell = row.createCell((short) 4);
            cell.setCellValue("年级");

            // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
            for(int i=0;i<list.size();i++){
                HashMap map =(HashMap)list.get(i);
                // 第四步，创建单元格，并设置值
                row = sheet.createRow((int) i+1);
                HSSFCell celli = row.createCell((short) 0);
                row.createCell((short) 0).setCellValue(i);
                if(map.get("JGOBJECTNAME")!=null)
                    row.createCell((short) 1).setCellValue((String) map.get("JGOBJECTNAME"));
                if(map.get("ZHUSUO")!=null)
                    row.createCell((short) 2).setCellValue((String) map.get("ZHUSUO"));
                if(map.get("SHXYDM")!=null)
                    row.createCell((short) 3).setCellValue((String) map.get("SHXYDM"));
                if(map.get("LXR")!=null)
                    row.createCell((short) 4).setCellValue((String) map.get("LXR"));
            }
            // 第六步，将文件存到指定位置
            try {
                fileName = "E:/"+ UUID.randomUUID().toString()+".xls";
                FileOutputStream fout = new FileOutputStream(fileName);
                wb.write(fout);
                fout.close();
                wb.close();
            } catch (Exception e)  {
                e.printStackTrace();
            }
            return fileName;
        }




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

    /**
     * ExportExcelMapToQiNiu
     * @param content
     * @param fileTitel
     * @param mapKeys
     * @param fileName
     */
    public static String exportExcelMapToQiNiu(List<Map<String, Object>> content, String[] fileTitel, String mapKeys, String fileName) {
        XSSFWorkbook wb = new XSSFWorkbook(); // --->创建了一个excel文件
        XSSFSheet sheet = wb.createSheet(fileName); // --->创建了一个工作簿

//        sheet.autoSizeColumn(1);
//        sheet.autoSizeColumn(1, true);

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
        sheet.setColumnWidth(0, 20 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(1, 20 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(2, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(3, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(4, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(5, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(6, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(7, 25 * 256);  //设置列宽，20个字符宽
        fileName += ".xlsx";
        try {
//            response.addHeader("Content-Disposition",
//                    "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
//            OutputStream os = response.getOutputStream();
//            wb.write(os);
//            os.flush();
//            os.close();

            FileOutputStream fout = new FileOutputStream("/tmp/"+fileName);
            wb.write(fout);
            fout.close();
            wb.close();

//           byte[] fileByte = toByteArray3("/tmp/"+fileName);
//
//
//           String qiNiuResult = QiNiuUtil.upload(fileName,fileByte);



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e)
 {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] toByteArray3(String filename) throws IOException {

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                    fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                // System.out.println("remain");
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
