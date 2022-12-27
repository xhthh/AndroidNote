package com.xht.androidnote.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

public class EncryptionUtil {
    static byte[] key = new byte[]{0x01, 0x06, 0x03, 0x04, 0x08, 0x04, 0x05, 0x04, 0x02, 0x01, 0x07, 0x01, 0x06, 0x02,
            0x03, 0x02};

    public static String encryption(String content) {
        SymmetricCrypto sm4 = SmUtil.sm4(key);
        String encryptHex = sm4.encryptHex(content);
        return encryptHex;
    }

    public static String decryption(String content) {
        SymmetricCrypto sm4 = SmUtil.sm4(key);
        String decryptStr = sm4.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
        return decryptStr;
    }

    public static void main(String[] args) {
        String content = "abc123";
        String encryption = encryption(content);
        System.out.println("encryption ------ " + encryption);

        String decryption = decryption(encryption);
        System.out.println("decryption ------ " + decryption);
    }
}
