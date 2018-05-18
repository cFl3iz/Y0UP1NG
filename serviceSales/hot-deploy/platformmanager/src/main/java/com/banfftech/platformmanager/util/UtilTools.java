package main.java.com.banfftech.platformmanager.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/9/26.
 */
public class UtilTools {

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
