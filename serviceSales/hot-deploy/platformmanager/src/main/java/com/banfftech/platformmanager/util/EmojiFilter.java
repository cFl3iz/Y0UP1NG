package main.java.com.banfftech.platformmanager.util;


/**
 * @author S.Y.L
 * emoji表情字符检查与过滤工具
 */
public class EmojiFilter {


    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (source ==null || source.trim().equals("")) {
            return false;
        }
        int len = source.length();
        System.out.println("len="+len+source);
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            System.out.println("i="+isNotEmojiCharacter(codePoint));
            if (!isNotEmojiCharacter(codePoint)) {
                //判断到了这里表明，确认有表情字符
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为非Emoji字符
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }






    public static void main(String[] args) throws Exception {

    }




        /**
         * 过滤emoji 或者 其他非文字类型的字符
         *
         * @param source
         * @return
         */
    public static String filterEmoji(String source) {
        if (source ==null || source.trim().equals("")) {
            return source;
        }
        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        StringBuilder buf = new StringBuilder();
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isNotEmojiCharacter(codePoint)) {
                buf.append(codePoint);
            }
        }

        return buf.toString().trim();
    }
}