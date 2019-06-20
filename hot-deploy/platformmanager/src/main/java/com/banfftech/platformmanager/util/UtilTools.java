package com.banfftech.platformmanager.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.UtilDateTime;
/**
 * Created by Administrator on 2017/9/26.
 */
public class UtilTools {





    /**
     * List Paging
     * @param list
     * @param viewIndex
     * @param viewSize
     * @param <T>
     * @return <T>List<T>
     */
    public static <T>List<T> getListPaging(List<T> list,Integer viewIndex,Integer viewSize){
        if(UtilValidate.isEmpty(list)){
            return null;
        }
        if(viewIndex==null||viewSize==null){
            return list;
        }
        Integer fromIndex = (viewIndex-1==-1?0:viewIndex)*viewSize;
        Integer toIndex = (viewIndex+1)*viewSize>list.size()?list.size():(viewIndex+1)*viewSize;
        return list.subList(fromIndex,toIndex);
    }

    /**
     * Sub List
     * @param genericValues
     * @param skip
     * @param top
     * @return
     */
    public static List<GenericValue> getListPaging(List<GenericValue> genericValues,int skip, int top) {

        if(skip>0){
            if(skip>genericValues.size()){
                return null;
            }
            genericValues = genericValues.subList(skip,genericValues.size());
        }
        if(top>0){
            genericValues = genericValues.subList(0,top>genericValues.size()?genericValues.size():top);
        }

        return genericValues;
    }


    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static Timestamp dateStringToTimestamp(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date parsedDate = sdf.parse(dateString);
            return UtilDateTime.toTimestamp(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 解密
     * @param key
     * @param iv
     * @param encData
     * @return
     * @throws Exception
     */
    public static void decrypt(byte[] key, byte[] iv, byte[] encData) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
//解析解密后的字符串
        String data = new String(cipher.doFinal(encData),"UTF-8");
        System.out.println("decode => " + data);
    }

     public static String dateToStr(java.sql.Timestamp time, String strFormat) {
             DateFormat df = new SimpleDateFormat(strFormat);
             String str = df.format(time);
             return str;
         }
    /**
     * 替换空格
     * @param str
     * @return
     */
    public static String replaceBlank(String str){
        String dest = null;
        if(str == null){
            return dest;
        }else{
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            return dest;
        }
    }


    /**
     * UTF-8编码 转换为对应的 汉字
     *
     * @param s E69CA8
     * @return 木
     */
    public static String convertUTF8ToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        try {
            s = s.toUpperCase();
            int total = s.length() / 2;
            //标识字节长度
            int pos = 0;
            byte[] buffer = new byte[total];
            for (int i = 0; i < total; i++) {
                int start = i * 2;
                //将字符串参数解析为第二个参数指定的基数中的有符号整数。
                buffer[i] = (byte) Integer.parseInt(s.substring(start, start + 2), 16);
                pos++;
            }
            //通过使用指定的字符集解码指定的字节子阵列来构造一个新的字符串。
            //新字符串的长度是字符集的函数，因此可能不等于子数组的长度。
            return new String(buffer, 0, pos, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GB2312
                String s = encode;
                return s;      //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {   //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }
}
