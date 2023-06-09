package com.ytaoCrow.ssooauth.utils;

import java.security.MessageDigest;
import java.util.UUID;

public class Md5Utils {

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f"};

    /**
     * 獲取MD5雜湊值的方法
     *
     * @param origin
     * @param charsetname
     * @return
     */
    public static String md5Hex(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static void main(String[] args) {
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        String passWord = "123456&" + salt;
        String realPassWord = Md5Utils.md5Hex(passWord, "UTF-8");
        System.out.println("salt->" + salt + ";realPassword->" + realPassWord);
    }
}
