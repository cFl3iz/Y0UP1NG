package main.java.com.banfftech.platformmanager.util;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author S.Y.L
 */
public class EmojiHandler {

    public static void main(String[] args) throws Exception {
        byte[] testbytes = {105,111,115,-30,-102,-67,32,36,-18,-128,-104,32,36,-16,-97,-113,-128,32,36,-18,-112,-86};
        String tmpstr = new String(testbytes,"utf-8");
        System.out.println(tmpstr);

        String encoded = encodeJava(tmpstr);
        System.out.println(encoded);
        System.out.println(decodeJava(encoded));

    }

    public static String decodeJava(String text) {
        return StringEscapeUtils.unescapeJava(text);
    }

    public static String encodeJava(String text) {
        return StringEscapeUtils.escapeJava(text);
    }

    public String decodeHtml(String emoji) {
        return StringEscapeUtils.unescapeHtml(emoji);
    }

    public String encodeHtml(String emoji) {
        return StringEscapeUtils.escapeHtml(emoji);
    }



    public String encodeToUNICODE(String text) {
        String encoding = "UNICODE";
        try {
            byte[] bytes = (text).getBytes();
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }

    public String encodeToUSASCII(String text) {
        String encoding = "US-ASCII";
        try {
            byte[] bytes = (text).getBytes();
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }

    public String encodeToUTF8(String text) {
        String encoding = "UTF-8";
        try {
            byte[] bytes = (text).getBytes();
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }

    public String encodeToUTF16(String text) {
        String encoding = "UTF-16";
        try {
            byte[] bytes = (text).getBytes();
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }

    public static String toUnicode(char c) {
        return String.format("\\u%04x", (int) c);
    }
}
