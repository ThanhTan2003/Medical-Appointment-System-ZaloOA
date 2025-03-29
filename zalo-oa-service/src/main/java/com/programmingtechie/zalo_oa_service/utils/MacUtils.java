package com.programmingtechie.zalo_oa_service.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MacUtils {

    public static String buildMac(String appId, String data, String timeStamp, String secretKey) {
        String[] lstParams = new String[] {appId, data, timeStamp, secretKey};
        String mac = buildMacForAuthentication(lstParams);
        return mac;
    }

    public static String buildMacForAuthentication(String[] lstParams) {
        String res = "";
        for (String temp : lstParams) {
            res += temp;
        }
        return encodeSHA256(res);
    }

    public static String encodeSHA256(String input) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes());
            byte byteData[] = md.digest();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return sb.toString();
    }
}
