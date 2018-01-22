package main.java.com.banfftech.platformmanager.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/9/26.
 */
public class UtilTools {



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
