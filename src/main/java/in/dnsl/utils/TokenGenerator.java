package in.dnsl.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * 生成一个安全且不重复的Token
 * @author dnslin
 */
public class TokenGenerator {


    private static final SecureRandom secureRandom = new SecureRandom(); // 线程安全
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); // URL安全


    /**
     * 生成一个安全且不重复的Token
     * @return 生成的Token字符串
     */
    public static String generateToken() {
        // 创建源数据，包括随机数和当前时间戳
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        long currentTimeMillis = System.currentTimeMillis();

        // 合并随机数和时间戳为一个字节数组
        byte[] data = mergeArrays(randomBytes, longToBytes(currentTimeMillis));

        // 使用SHA-256处理数据
        byte[] sha256Bytes = sha256(data);

        // 返回Base64编码的字符串
        return base64Encoder.withoutPadding().encodeToString(sha256Bytes);
    }

    /**
     * 将两个字节数组合并为一个
     * @param array1 第一个数组
     * @param array2 第二个数组
     * @return 合并后的数组
     */
    private static byte[] mergeArrays(byte[] array1, byte[] array2) {
        byte[] mergedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, mergedArray, 0, array1.length);
        System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
        return mergedArray;
    }

    /**
     * 将长整型转换为字节数组
     * @param value 要转换的长整型
     * @return 字节数组
     */
    private static byte[] longToBytes(long value) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(value & 0xFF);
            value >>= 8;
        }
        return result;
    }

    /**
     * 使用SHA-256哈希算法处理数据
     * @param data 要处理的数据
     * @return 处理后的数据
     */
    private static byte[] sha256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public static void main(String[] args) {
        String token = generateToken();
        System.out.println("Generated Secure Token: " + token);
    }
}
