package io.github.hellomaker.launcher.common;

import com.alibaba.fastjson2.JSON;
import io.github.hellomaker.launcher.verify.VerifyInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hellomaker
 */
@Slf4j
public class SymmetricEncryption {

    /**
     * 密钥
     */
    private static final String SECRET_KEY = "w8e10wh73se4omvf";

    public static String encodeSerialNumber(String hexStr) {
        char[] charArray = hexStr.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == 'Z') {
                charArray[i] = 'A';
            } else if (c == '9') {
                charArray[i] = '0';
            } else if (c == 'z') {
                charArray[i] = 'a';
            } else {
                charArray[i] = (char) (c + 1);
            }
            if (i != 0 && i % 5 == 0) {
                stringBuilder.append('-');
            }
            stringBuilder.append(charArray[i]);
        }
        System.out.println("encode : " + stringBuilder);
        return stringBuilder.toString();
    }

    public static String encodeVerifyNumber(VerifyInfo verifyInfo) {
        String jsonString = JSON.toJSONString(verifyInfo);
        return AESUtil.encrypt(jsonString, verifyKey(verifyInfo.getSerialNumber()));
    }

    public static VerifyInfo verifyInfo(String hexStr, String verifyNumber) {
        try {
            String decrypt = AESUtil.decrypt(verifyNumber, verifyKey(hexStr));
            return JSON.parseObject(decrypt, VerifyInfo.class);
        } catch (Exception e) {
            log.error("解析 verifyinfo 错误", e);
            return null;
        }
    }

    public static boolean verifyNumber(String hexStr, String verifyNumber) {
        try {
            VerifyInfo verifyInfo = verifyInfo(hexStr, verifyNumber);
            return verifyInfo != null && verifyInfo.getSerialNumber() != null && hexStr.equals(verifyInfo.getSerialNumber());
        } catch (Exception e) {
            return false;
        }
    }

    private static String verifyKey(String hexStr) {
        char[] charArray = SECRET_KEY.toCharArray();
        String lowerStr = hexStr.toLowerCase();
        char[] charArray2 = lowerStr.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (i < charArray2.length) {
                char c1 = charArray2[i];
                if (c1 == '-') {
                    c = c;
                } else if (c1 >= 'a' && c1 <= 'z') {
                    c = (char) (c + c1 - 'a');
                } else if (c1 >= '0' && c1 <= '9') {
                    c = (char) (c + c1 - '0');
                }
            }

            if (c > '9' && c < 'a') {
                c = (char) (c - '9');
            }
            if (c > 'z') {
                c = (char) (c - 'z');
            }

            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}
