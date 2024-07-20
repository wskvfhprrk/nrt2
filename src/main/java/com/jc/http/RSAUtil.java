package com.jc.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**
 * RSAUtil
 *
 * @version V1.0
 */
public final class RSAUtil {
    private RSAUtil() {
    }

    public enum SignAlgorithm {
        SHA256withRSA,
        MD5withRSA,
        ;
    }

    private static final Charset charset = StandardCharsets.UTF_8;


    public static final String ALGORITHM = "RSA";


    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    private static final KeyFactory keyFactory;

    static {
        try {
            keyFactory = KeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static PrivateKey getPrivateKey(String privateKey) {
        try {
            byte[] privateKeyBase64 = Base64.getDecoder().decode(removeLineTokenizer(privateKey).getBytes(charset));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBase64);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey getPublicKey(String publicKey) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(removeLineTokenizer(publicKey).getBytes(charset));
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String removeLineTokenizer(String key) {
        return key.replace("\r", "").replace("\n", "");
    }

    /**
     * 签名器
     */
    public static final class Signer {
        private Signer() {
        }

        /**
         * RSA签名
         *
         * @param content    待签名数据
         * @param privateKey 密钥
         * @return 签名值
         */
        private static String sign(String content, PrivateKey privateKey, SignAlgorithm signAlgorithm) {
            try {
                Signature signature = Signature.getInstance(signAlgorithm.name());
                signature.initSign(privateKey);
                signature.update(content.getBytes(charset));
                return new String(Base64.getEncoder().encode(signature.sign()), charset);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 对返回参数进行加签
         *
         * @param content
         * @param privateKey
         * @return
         */
        public static String sign(String content, String privateKey, SignAlgorithm signAlgorithm) {
            return sign(content, getPrivateKey(privateKey), signAlgorithm);
        }

        /**
         * RSA验签名检查
         *
         * @param content   待签名数据
         * @param sign      签名值
         * @param publicKey 分配给开发商公钥
         * @return 布尔值
         */
        public static boolean verify(String content, String sign, PublicKey publicKey, SignAlgorithm signAlgorithm) {
            if (sign == null) {
                return false;
            }
            try {
                Signature signature = Signature.getInstance(signAlgorithm.name());
                signature.initVerify(publicKey);
                signature.update(content.getBytes(charset));
                return signature.verify(Base64.getDecoder().decode(sign.getBytes(charset)));
            } catch (Exception e) {
                // ignore error
            }
            return false;
        }

        public static boolean verify(String content, String sign, String publicKey, SignAlgorithm signAlgorithm) {
            return verify(content, sign, getPublicKey(publicKey), signAlgorithm);
        }
    }
}
