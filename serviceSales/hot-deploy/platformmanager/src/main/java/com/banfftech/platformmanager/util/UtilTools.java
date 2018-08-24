package main.java.com.banfftech.platformmanager.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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
}
