package io.github.hellomaker.launcher.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.github.hellomaker.launcher.controller.LicenseController;
import io.github.hellomaker.launcher.verify.VerifyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author hellomaker
 */
public class SymmetricEncryption {

    static Logger log = LoggerFactory.getLogger(SymmetricEncryption.class);

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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serialNumber", verifyInfo.getSerialNumber());
        jsonObject.put("vaildDate", verifyInfo.getValidDate());
        jsonObject.put("vaildTimes", verifyInfo.getValidTimes());
        jsonObject.put("vaildSubSystemNameList", StringUtil.combineArrToStr(verifyInfo.getValidSubSystemNameList()));
        String jsonString = jsonObject.toJSONString();
//                JSON.toJSONString(verifyInfo);
        return AESUtil.encrypt(jsonString, verifyKey(verifyInfo.getSerialNumber()));
    }

    public static VerifyInfo verifyInfo(String hexStr, String verifyNumber) {
        try {
            String decrypt = AESUtil.decrypt(verifyNumber, verifyKey(hexStr));
            JSONObject parse = JSONObject.parse(decrypt);
            VerifyInfo verifyInfo = new VerifyInfo();
            assert parse != null;
            verifyInfo.setSerialNumber(parse.getString("serialNumber"));
            verifyInfo.setValidDate(parse.getString("vaildDate"));
            verifyInfo.setValidTimes(parse.getLong("vaildTimes"));
            String vaildSubSystemNameList = parse.getString("vaildSubSystemNameList");
            if (vaildSubSystemNameList != null) {
                List<String> strings = StringUtil.splitList(vaildSubSystemNameList, ",");
                verifyInfo.setValidSubSystemNameList(strings);
            }

//            verifyInfo.setValidMenuNameList(List.of());
//            return JSON.parseObject(decrypt, VerifyInfo.class);
            return verifyInfo;
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
