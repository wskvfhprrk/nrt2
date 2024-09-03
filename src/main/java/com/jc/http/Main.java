package com.jc.http;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Main {

    private static final String privateKeyString = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCHer9bJenBGT813uR2FD62X9URkQIRcREkIEV6PYLEJ5XvkOrhQYHg1vRa/huhCmHkUR9Vjq/cWyv27AauO7qJBeuoGErhf7aBMq1Vjf5uwaBNfMKy28mxTdnIosEL0z6XmFgt3h3tk2TrB0hzVDDSfZdyE5dZ3dpSJwkd2WREf3Tom9KKzXEIBrBKIMuQ0Iu+Fp9iz84guPnYNEcSAJf1+AkjZKagChdQxS0wgfp5lQFsusarzXVg1/HpTSNlIKSYgJCc+C+MexfCoVCYtNmkheii1ZBEqiF211YqczT5g+F/UHdiRg2Ec5X9yXoiCNr9do1HIZ7G4lZQSIZBxCQ9AgMBAAECggEAUrt1oZ68aYwWWrpbf3QLe+l3vvtzbN1EH3CJnQV1Fn4qBJrVpGsRehEqNWrZynUBpKCZZvD0mbcfWPF3fuzAk66G4ya6i4wnEiy1RvqoNlCNqOQYLYskVt74sJobEzKUFZUVvCTY19zbkt7msm7mRZQmaKZoFZvyF5r46T55CY5xV+SXKFYggJVvq+hIFLpu7M3VTvnYk8zRvOC5hrGeABgTQzOX5DnNYeudyo813mQD0+YFDZ1iZYO7zvltXDtisZqATUTmeXdY5o8JJc2Z8kZkLU+Zat5b7m9u/8yT38Tug+K+axLLgG6fRzzcWOd71s8C9hphAMj0SLgGEjbqwQKBgQD+lYjvfSjGLWmaMv7R9a/xE5J5bYbb7q4tfFfHSqkKxrwVngi+I67H8YdCXJcAUnbjpWqiZEwudrtclsaBNpZrjC7LwFHRNQ0C0cc0NsDoMV5crWm9Hf9wPjKTUf5Dk+j1eEk3U9Z+NB73ga7sccMfUgEb7Fs9BSmD5Qf6dxfcrQKBgQCIO6MMN1WwaHjrBQLmdFDOGZbLNCz9PQQq/H2nTqfYF03KCXT9IP7S0YLwXBo1DJqzywy3C9Pyd3hffxpEEFSJS0fCRC9DwtmWetWYwozZn4T5Ic8VTvrWSoa54eG7lGkfT3zh7LEviRFmDFCr9X94h1to8TjFt0uzgY4lIwlH0QKBgDaRGJO0cOvhSfQ6H/Iixf5XIpsSRciYJQ8syqe1sfvUUvYTNG6EuW6zrzkjHV59TmxGxU9fThDbdBdJXezOJXmRx2n2o9LCmzJSgm8HmVtrUX1t0e8uIdUOD8sR1PzpFBKhIJhSSPQt+cE1M6gMTjVFX2V7yKaQl7Bkg8xEgIHVAoGATKtbeJ2PsQcqvgGE/1OIwmTuU5Aana6AMP0Gbmk61sdOtRybZXXzU4wrp7/908szKWplzoJulq+b5AqxKosSOG5QhUlAXF9Fe4XHvgAHqtY2zMq1M6XOT1mD9wrjsmC0xLVEcBV4Jmt6ijb2E8DHK3rp++Z97+/XP7/y0n2kVHECgYBEn/xYM6cLL8bWRAo6hrncJmxYSk1ceqYws0wyQP0PymPdnKY+drjtnfV/0C0m8s7NtW/B88uSxn4Bh6ZfbEJMu0Dh2xknZHL5Db+ioTEIkTjXt/p9Gl8hRbU/BmZnIBbV2QOnBYav/FvLjmLl12vFNjH+dweJtB7Dy0D2CN+4Uw==";
    private static final String timestamp = "1689651147";
    private static final String nonce = "345tgyve5r67";
    private static final String body = "{\"partnerId\":2725,\"appId\":\"1231321\",\"authMode\":\"mobile\",\"authCode\":15216868575}";

    public static void main(String[] args) {
        Signer signer = new Signer("SHA-256");
        try {
            signer.rsasign();
        } catch (Exception e) {
            System.out.println("签名失败: " + e.getMessage());
        }
    }

    static class Signer {
        private String hashAlgorithm;

        public Signer(String hashAlgorithm) {
            this.hashAlgorithm = hashAlgorithm;
        }

        public void rsasign() throws Exception {
            String content = timestamp + ";" + nonce + ";" + body + ";";
            System.out.println("待加密字符串: " + content);

            // 签名
            String signed = sign(content, privateKeyString);
            System.out.println("签名: " + signed);
        }

        public String sign(String content, String rsaPrivateKeyString) throws Exception {
            MessageDigest digest = MessageDigest.getInstance(hashAlgorithm);
            byte[] hashed = digest.digest(content.getBytes(StandardCharsets.UTF_8));

            byte[] privateKeyBytes = Base64.getDecoder().decode(rsaPrivateKeyString);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = java.security.KeyFactory.getInstance("RSA").generatePrivate(keySpec);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(hashed);

            byte[] signedBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signedBytes);
        }
    }
}
