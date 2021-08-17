package cn.atsoft.dasheng.uc.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    private static final String charset = "UTF-8";

    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String ALGORITHMSTR = "AES/CBC/PKCS5Padding";

    /**
     * Description: AES解密
     */
    public static String decrypt(String content, String encryptKey, String siv) throws Exception {
        try {
            if (content == null || content.length() == 0) {
                return null;
            }
            if (encryptKey == null) {
                throw new Exception("decrypt key is null");
            }
            if (encryptKey.length() != 16) {
                throw new Exception("decrypt key length error");
            }
            byte[] Decrypt = ByteFormat.hexToBytes(content);
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            SecretKeySpec skeySpec = new SecretKeySpec(encryptKey.getBytes(charset), "AES");
            IvParameterSpec iv = new IvParameterSpec(siv.getBytes(charset));//new IvParameterSpec(getIV());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);//使用解密模式初始化 密
            return new String(cipher.doFinal(Decrypt), charset);
        } catch (Exception ex) {
            throw new Exception("decrypt errot", ex);
        }
    }
}
