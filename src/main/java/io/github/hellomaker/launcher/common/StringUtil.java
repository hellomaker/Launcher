package io.github.hellomaker.launcher.common;

public class StringUtil {

    public static String[] split(String content, String regix) {
        return content.split(regix);
    }

    public static Integer[] splitAndToIntegerArr(String content, String regex) {
        String[] arr = content.split(regex);
        Integer[] result = new Integer[arr.length];
        for (int i=0; i<result.length; i++) {
            result[i] = Integer.valueOf(arr[i]);
        }
        return result;
    }

    public static int[] splitAndToIntArr(String content, String regex) {
        String[] arr = content.split(regex);
        int[] result = new int[arr.length];
        for (int i=0; i<result.length; i++) {
            result[i] = Integer.valueOf(arr[i]);
        }
        return result;
    }

    public static Integer[] splitAndToIntegerArr(String content) {
        return splitAndToIntegerArr(content, ",");
    }

    public static int[] splitAndToIntArr(String content) {
        return splitAndToIntArr(content, ",");
    }

    public static String combineArrToStr(Integer[] arr) {
        if (arr == null) {
            return null;
        }
        if (arr.length == 0) {
            return "";
        }
        StringBuilder res = new StringBuilder("" + arr[0]);
        for (int i=1; i<arr.length; i++) {
            res.append(",");
            res.append(arr[i]);
        }
        return res.toString();
    }

    public static String combineArrToStr(String[] arr) {
        if (arr == null) {
            return null;
        }
        if (arr.length == 0) {
            return "";
        }
        String res = arr[0];
        for (int i=1; i<arr.length; i++) {
            res += ",";
            res += arr[i];
        }
        return res;
    }

    /**
     转半角的函数(DBC case)<br/><br/>
     全角空格为12288，半角空格为32
     其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * @param input 任意字符串
     * @return 半角字符串
     *
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                //全角空格为12288，半角空格为32
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                //其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     转全角的方法(SBC case)<br/><br/>
     全角空格为12288，半角空格为32
     其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * @param input 任意字符串
     * @return 半角字符串
     *
     */
    public static String ToSBC(String input)
    {
        //半角转全角：
        char[] c=input.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            if (c[i]==32)
            {
                c[i]=(char)12288;
                continue;
            }
            if (c[i]<127)
                c[i]=(char)(c[i]+65248);
        }
        return new String(c);
    }



    /**
     * 将字符串转换成ASCII码
     * @param cnStr
     * @return String
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        // 将字符串转换成字节序列
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            // 将每个字符转换成ASCII码
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }
    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String capitalize(String str) {
        char ch[];
        ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        String newString = new String(ch);
        return newString;
    }



    /**
     * 功能描述: 检测是否有符号
     *
     * @author  曲胜健
     * @param   ch  字符
     * @date    2023-05-19 18:04
     * @return  boolean
     */
    public static boolean hasSpecialChar(char ch) {
        String regex ="[\\p{P}\\p{S}]+";
        if(String.valueOf(ch).matches(regex)){
            return true;
        }else{
            String specialChars = "~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?[]";
            return specialChars.indexOf(ch) != -1;
        }
    }

    /**
     * 功能描述: 去除特殊符号
     *
     * @author  曲胜健
     * @param   ch
     * @date    2023-05-22 14:00
     * @return  char
     */
    public static char removeSpecialChar(char ch) {
        char[] specialChars = { '~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '[', '{', ']', '}', '\\', '|', ';', ':', '\'', '\"', ',', '<', '.', '>', '/', '?' ,' '};
        for (char c : specialChars) {
            if (c == ch) {
                return '\0';
            }
        }
        return ch;
    }

}