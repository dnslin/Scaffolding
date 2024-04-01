package in.dnsl.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
@Slf4j
public class PasswordUtils {

    // 生成盐的方法
    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // 使用SHA-256和盐来加密密码的方法
    public static String generateSecurePassword(String password, String salt) {
        String encryptedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            encryptedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("加密密码失败", e);
        }
        return encryptedPassword;
    }

    // 验证输入的密码是否与存储的加密密码匹配的方法
    public static boolean verifyUserPassword(String providedPassword,
                                             String securedPassword, String salt) {
        // 生成输入密码的加密版本
        String newSecurePassword = generateSecurePassword(providedPassword, salt);
        // 比较加密后的输入密码与存储的加密密码
        return newSecurePassword.equals(securedPassword);
    }
}
