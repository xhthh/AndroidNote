package com.xht.androidnote.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA加密的工具类
 */

public class RSAEncryptUtils {
    public static final String RSA = "RSA";
    public static final String OTHER = "RSA/None/PKCS1Padding";
    private static String charset = "utf-8";
    private static final String RSA_PUBLIC_EY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCT60R14B1hCkZa+0ZFr/FB0C41srPi1JpT1m05WudYPMPRDuBAvV3MKzD9UtYPs84PV9O+xI0RLS2U+I3Ql/hRMTfWEQ5BmC9OFbI1DWst1D1r7MOSN90sbHVGL9Xo2E4CvCBZ/26s+bdCmTewVaVIsLHsocuTYMRXKPF940c3KwIDAQAB";

    private static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAshvtfW0/0itm6u8Gw+RKfS8l9uD+W92mlgUYz2JZlUgWqdfF5/8S6zTgjIBL1X6VENuE+a8pMg73KmH03pENFk5QL/BQ8Po/i1PHsSXQCr3DSeozTUr9bfmxUX7TDx+MVTvmGnabtw8EwoWZxzOObVr+dlogITwqp2B5iVJEGjzW0GvGSmj11wZMPoxneSJD+HRqJUGDRpUJRIwHT3wzl2oRVE7l+Y3KFtEQnco5e5jQpZxl3XJnDxZTnuwys8SeAjDkLhU6BX25aCXJDYI55zhkEsqGX70xBtdDrCqsMvxschTfxucGIHvmsAnqutUrBtLp8OFK/aC7L4/57NQJOQIDAQAB";
    private static String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCyG+19bT/SK2bq7wbD5Ep9LyX24P5b3aaWBRjPYlmVSBap18Xn/xLrNOCMgEvVfpUQ24T5rykyDvcqYfTekQ0WTlAv8FDw+j+LU8exJdAKvcNJ6jNNSv1t+bFRftMPH4xVO+Yadpu3DwTChZnHM45tWv52WiAhPCqnYHmJUkQaPNbQa8ZKaPXXBkw+jGd5IkP4dGolQYNGlQlEjAdPfDOXahFUTuX5jcoW0RCdyjl7mNClnGXdcmcPFlOe7DKzxJ4CMOQuFToFfbloJckNgjnnOGQSyoZfvTEG10OsKqwy/GxyFN/G5wYge+awCeq61SsG0unw4Ur9oLsvj/ns1Ak5AgMBAAECggEACGBNqHIcjlurlBpLcdrI8Z80hJCrPBKUYYZgcGxaWy3QBwr82MuQDhvnM/EneXH7b7BrNG506QPdwt8z3v21i42BfuepEgdeBd7msfsqkAuOZVAy1Nk7sIVA8UMeIoSkATD3Qg2LucdM7+fMxhrNTj5GLLaE8uf7kSVb23kd3WZX9keaAfU7P7bJz/9Y8cqCD/cqq5OnK1J/uBxUSGuNhwA7vgOxdMv94s3UtUPhVyt9g/2k4Vuq+VxAnCLG1Zs0cvYymvWp5y3GpPXmzNgqIpUIj9HO8oHL181u6V3EYc/+/hTQMhA0jyllh6iBS47p71X5Sg1jcFvcA+fE/82HswKBgQDgGn0F7LOGL+peJmh6OW0pee7j+hSEOnYQTsN2+E6YL1omz/Ca/RwvsmAZWIJVcnuanUZKpvdoNEDNEhiCKjC3Y0BIP8FyG2rx666/KEs3ypiMhdey6BG8xo/kwoU5DY09j1ht5qVmG/0kv3kpMlpvOEMq/bq+T/EgVTT07XJvWwKBgQDLdZJ/WY+PYuWdYKWfMaY28+7iEBrAnw6l0ORym/L2a2Epeyds/ebRs9hw/MlsOa9oGDRHPAy/OnNq01ifJ4FXMfgg+Nykc9BcwlCaBIgRhh8vsrGPX5ZC43PfjnKpqQ2rhPxLj+/gE4yhHBgsSQMpIGzKxGGW6FW+gV211cyB+wKBgDGSim9iEcAwHzbeZiQLSfNvzoFNk3ZAfFdVJPXPLYuKhfWp775JGRAP9CQHKSV/oonsGl1vG7FIRcFXLGa2sHoQkWqp97+nAvwqt0f/wMvBnAcRoEop/ap1feULhm69aa3hZJ1C9PlmXZPgawvK4KhTmMAMyl9IzbzqPEGluLebAoGAQpj9lwnt3/VzS4xzKiTKT3qRWxYCa8KkvMofvRQm91o2t6lEQnzprQK9el6EYBSRwfUKR7b0RTQQy4ram14HDE+i9MgBESOHtJU2ZPPOxk2sr6RCUrfrK4HgT/EEv1xCyT4Ch4lZ73L2uVXE49oSWsfuVIDroMX0KyOVVQvioTkCgYA8IocArqCm62YKLoliITNbVNwIrcU/klVBFngRNJlZISkChjamsT1luqKZ53eYqlH4heIt01DhsuSn2UINtY1JoQKVuU+xJMpqJijkFY9WZMrGurLgN5x0EOTV9PUS0yRUBB/54zj11ovScgJtye2zbVI/T7/ytjpOnYsIpDhaAg==";

    /**
     * 生成公钥，将公钥转换为字符串
     */
    private void testRSA() {
        System.out.println("Convert RSA public key into a string an dvice versa");
        // generate a RSA key pair
        KeyPairGenerator keygen = null;
        try {
            keygen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keygen.initialize(2048, new SecureRandom());
        KeyPair keyPair = keygen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Log.e("rsa", "publicKey: " + publicKey);
        // get encoded form (byte array)
        byte[] publicKeyByte = publicKey.getEncoded();
        byte[] privateKeyByte = privateKey.getEncoded();
        // Base64 encoded string
        String publicKeyString = Base64.encodeToString(publicKeyByte, Base64.DEFAULT);
        String privateKeyString = Base64.encodeToString(privateKeyByte, Base64.DEFAULT);
        Log.e("rsa", "publicKeyString: " + publicKeyString);
        Log.e("rsa", "privateKeyString: " + privateKeyString);

        String text = "hello 测试一下rsa";
        try {
            String encrypt = RSAEncryptUtils.encryptByPublicKey(text, RSAEncryptUtils.loadPublicKey(publicKeyString));
            Log.e("rsa", "encrypt: " + encrypt);

            String decrypt = RSAEncryptUtils.decryptByPrivateKey(encrypt, RSAEncryptUtils.loadPrivateKey(privateKeyString));
            Log.e("rsa", "decrypt: " + decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从字符串中加载公钥,从服务端获取
     *
     * @param pubKey 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKey(String pubKey) {
        if (TextUtils.isEmpty(pubKey)) {
            pubKey = publicKey;
        }
        try {
            byte[] buffer = Base64.decode(pubKey, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥加密过程
     *
     * @param plainData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptWithRSA(String plainData) {
        try {
            PublicKey publicKey = loadPublicKey(RSA_PUBLIC_EY);
            if (publicKey == null) {
                return "";
            }
            Cipher cipher = null;
            cipher = Cipher.getInstance(OTHER);// 此处如果写成"RSA"加密出来的信息JAVA服务器无法解析
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] ciphertext = cipher.doFinal(plainData.getBytes());
            String ret = Base64.encodeToString(ciphertext, Base64.DEFAULT);
            ret = ret.replaceAll("\\n", "");
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encryptWithRSA(PublicKey publicKey, String plainData) {
        try {
            if (publicKey == null) {
                return "";
            }
            Cipher cipher = null;
            cipher = Cipher.getInstance(OTHER);// 此处如果写成"RSA"加密出来的信息JAVA服务器无法解析
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] ciphertext = cipher.doFinal(plainData.getBytes());
            String ret = Base64.encodeToString(ciphertext, Base64.DEFAULT);
            ret = ret.replaceAll("\\n", "");
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 单独添加为Native用的一个加密方法
     *
     * @param publicKey
     * @param plainData
     * @return
     * @throws Exception
     */
    public static String encryptWithRSAForNative(PublicKey publicKey, String plainData) throws Exception {
        if (publicKey == null) {
            throw new NullPointerException("encrypt PublicKey is null !");
        }
        Cipher cipher = null;
        cipher = Cipher.getInstance(OTHER);// 此处如果写成"RSA"加密出来的信息JAVA服务器无法解析
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        try {
            byte[] ciphertext = cipher.doFinal(plainData.getBytes());
            return Base64.encodeToString(ciphertext, Base64.NO_WRAP);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 公钥解密过程
     *
     * @param publicKey   公钥
     * @param encryedData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static String decryptWithRSA(PublicKey publicKey, String encryedData) throws Exception {
        if (publicKey == null) {
            throw new NullPointerException("decrypt PublicKey is null !");
        }
        Cipher cipher = null;
        cipher = Cipher.getInstance(OTHER);// 此处如果写成"RSA"解析的数据前多出来些乱码
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] output = cipher.doFinal(Base64.decode(encryedData, Base64.DEFAULT));
        return new String(output);
    }

    /**
     * 公钥解密过程
     *
     * @param privateKey
     * @param encryedData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static String decryptWithRSA(PrivateKey privateKey, String encryedData) throws Exception {
        if (privateKey == null) {
            throw new NullPointerException("decrypt privateKey is null !");
        }
        Cipher cipher = null;
        cipher = Cipher.getInstance(OTHER);// 此处如果写成"RSA"解析的数据前多出来些乱码
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] output = cipher.doFinal(Base64.encode(encryedData.getBytes(charset), Base64.DEFAULT));
        return new String(output);
    }

    /**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去11
     *
     * @param data      需加密数据的byte数据
     * @param publicKey 公钥
     * @return 加密后的byte型数据
     */
    public static byte[] encryptData(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(OTHER);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用私钥解密
     *
     * @param encryptedData 经过encryptedData()加密返回的byte数据
     * @param privateKey    私钥
     * @return
     */
    public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(OTHER);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 使用N、e值还原公钥
     *
     * @param modulus
     * @param publicExponent
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(String modulus, String publicExponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 使用N、d值还原私钥
     *
     * @param modulus
     * @param privateExponent
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String modulus, String privateExponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }


    /**
     * 从字符串中加载私钥<br>
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        if (TextUtils.isEmpty(privateKeyStr)) {
            privateKeyStr = privateKey;
        }
        try {
            byte[] buffer = Base64.decode(privateKeyStr, Base64.DEFAULT);
            // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(InputStream in) throws Exception {
        try {
            return loadPublicKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从文件中加载私钥
     *
     * @param in
     * @return 是否成功
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(InputStream in) throws Exception {
        try {
            return loadPrivateKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    /**
     * 读取密钥信息
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static String readKey(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
                sb.append('\r');
            }
        }

        return sb.toString();
    }
    /**************************** RSA 公钥加密解密**************************************/

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, key_len - 11);
        String mi = "";
        //如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        //如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for (byte[] arr : arrays) {
            ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }

    /**
     * ASCII码转BCD码
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    /**
     * BCD转字符串
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }
}
