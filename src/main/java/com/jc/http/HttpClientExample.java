package com.jc.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.UUID;

public class HttpClientExample {
    public static void main(String[] args) {
        Head head = new Head();
        head.setUrl("/opencenter/order/query/detailByCode");
        head.setNonce(UUID.randomUUID().toString());
        String timestamp = String.valueOf(System.currentTimeMillis());
        head.setTimestamp(timestamp);
        head.setDataJson(null);
        sendHttp(head);
    }

    public static void sendHttp(Head head) {
        String url = "https://open-center-platform-gateway.sandload.cn" + head.getUrl();
        String json = head.getDataJson();

        // Create HttpClient instance
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create HttpPost request
            HttpPost httpPost = new HttpPost(url);
            //计算sign
            //MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCMpkCiSiYFyDvBrEDHg7NvP/n0hInB/vmZ5L2rFR8JraZEudMC8fdYVXXx+DtNc0kdTx2CfARtFRIm1IXvM2bNPEl84YoD5m7hExI/kRza0Te4ti3fzECNwEHKMcz3EenaW/jURSP5dBQt9ZC7TN+rpnbNNav5YPrTDPUVDRLy2/Kn57YtI72K4Y8YiI+JO5rI3i9wIydYV63NSPY3ADJ1Mu60/MPdJ1KADnfvaNCu+pRuRO7fUKdb2zcTQQCFPpcWQhaOH8XKh3GhgcfXYvJcHA5HkPJl98/kCEVMqm6ubMWh/0mkvE5eouAqWx31+XRb0t/vBnCP7DqvjduHx/hnAgMBAAECggEAPmTOTXBZV71RDQSuJBP/uL2H0/or5nyHG8cGgOu4viFjnh9VRXwzvuMClJoLLLuU1sSvEaCUkhOD3ufuloOV+jvhWd3vzPUfgQdl0/867rNbZbyjsyE4DeoZBWp8Q6qVtEGSrBAT+miNPyotz3k1zD7nevdxWAq2moOoAXOwUT2q9V8WGvjwh84mrC07uGuMFyVgOHTf8xbKOq8Dy+TfKBNFYuAtocbrGs2iR/wc8jrkgXKzYEkm7YbEWAIHGruNF7+Q9T1nBPHaFdAU7nxrwYyW8r/1iEui2dLYX4QIt4p3mkCwcWPdUSjli3yvaatqZ5/2UZZnmArP/FHeCAtlmQKBgQC2U5xcZouSEY4JMmrStiiAdTfuRnW3jrOJ3sKcaPH85f0+LEkDrEr6gPFdR/ME2LSawrnCLyTQyn7mXqhkM5Ijn4CUfP3LnjSlKVsdG8d5epsqMokDaaBtKdHoHE0L7UNkuczck/moRc/iW30WqvQbTSNzUHNIMxnuO0lceP1EwwKBgQDFe28dNiigi1PM9KwfRazAwH030lyyOZG1Mwvju9uNGna0anfaD8PdnwpAP3JxJZ7TBSVS5R9VI8ejCcJG4dSI64lQgL8/otPyksb0Sc/POiLD8rwUxr3D1pzN9zAG6LADHTh8ofxGKWTkHEhUErz3d25qFCEwAHL0C+y0A6zzjQKBgQCz4kwLcfTT+VPsSPzRxXyyOHQlNwkfP8BtDwVssbHGNNckxyaGZC9ZdWws/zaKGpAdfG8vYbnt8UtAWDXFHohZcx214pe3k/AW33WQg37LoKaD/HXnkwf/i9oYKgvt73mlYBb5rVkMp0wR5PfGLIZIhVOrir0ih6BWxdF8VbZzZwKBgQCF7cZXi0lWR+dXUoAA3WHhfyns16+h90UIHExt8BNfyJjElogdzR9Kh/eNxvm4HoluF0lOxLDRLujPe2jUZYecLBD+FbKeV7FiC4T+W6iho428UjyX4okD7eJ+FKvvo5LrKcALVlgexSf1Q/ERg3vAYLDzJB+1zDgppoSrOOrsaQKBgCcH5ZU6qQpzOh0QdmSdf73i1uVAgW1djwdtumcylc6zNr8n/HYi67zuGB7pVaeQX3VvML+et6ZZdkMK2mckcv1n8jbRFJJqzVI9vAEqu2uGfz3C5VsFtUTB1NmmBrTJcqtwk05Ud9Dy8+80MCTcH7LBBklMapll1QQWdvOYaSu7
            String privateKey="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCMpkCiSiYFyDvBrEDHg7NvP/n0hInB/vmZ5L2rFR8JraZEudMC8fdYVXXx+DtNc0kdTx2CfARtFRIm1IXvM2bNPEl84YoD5m7hExI/kRza0Te4ti3fzECNwEHKMcz3EenaW/jURSP5dBQt9ZC7TN+rpnbNNav5YPrTDPUVDRLy2/Kn57YtI72K4Y8YiI+JO5rI3i9wIydYV63NSPY3ADJ1Mu60/MPdJ1KADnfvaNCu+pRuRO7fUKdb2zcTQQCFPpcWQhaOH8XKh3GhgcfXYvJcHA5HkPJl98/kCEVMqm6ubMWh/0mkvE5eouAqWx31+XRb0t/vBnCP7DqvjduHx/hnAgMBAAECggEAPmTOTXBZV71RDQSuJBP/uL2H0/or5nyHG8cGgOu4viFjnh9VRXwzvuMClJoLLLuU1sSvEaCUkhOD3ufuloOV+jvhWd3vzPUfgQdl0/867rNbZbyjsyE4DeoZBWp8Q6qVtEGSrBAT+miNPyotz3k1zD7nevdxWAq2moOoAXOwUT2q9V8WGvjwh84mrC07uGuMFyVgOHTf8xbKOq8Dy+TfKBNFYuAtocbrGs2iR/wc8jrkgXKzYEkm7YbEWAIHGruNF7+Q9T1nBPHaFdAU7nxrwYyW8r/1iEui2dLYX4QIt4p3mkCwcWPdUSjli3yvaatqZ5/2UZZnmArP/FHeCAtlmQKBgQC2U5xcZouSEY4JMmrStiiAdTfuRnW3jrOJ3sKcaPH85f0+LEkDrEr6gPFdR/ME2LSawrnCLyTQyn7mXqhkM5Ijn4CUfP3LnjSlKVsdG8d5epsqMokDaaBtKdHoHE0L7UNkuczck/moRc/iW30WqvQbTSNzUHNIMxnuO0lceP1EwwKBgQDFe28dNiigi1PM9KwfRazAwH030lyyOZG1Mwvju9uNGna0anfaD8PdnwpAP3JxJZ7TBSVS5R9VI8ejCcJG4dSI64lQgL8/otPyksb0Sc/POiLD8rwUxr3D1pzN9zAG6LADHTh8ofxGKWTkHEhUErz3d25qFCEwAHL0C+y0A6zzjQKBgQCz4kwLcfTT+VPsSPzRxXyyOHQlNwkfP8BtDwVssbHGNNckxyaGZC9ZdWws/zaKGpAdfG8vYbnt8UtAWDXFHohZcx214pe3k/AW33WQg37LoKaD/HXnkwf/i9oYKgvt73mlYBb5rVkMp0wR5PfGLIZIhVOrir0ih6BWxdF8VbZzZwKBgQCF7cZXi0lWR+dXUoAA3WHhfyns16+h90UIHExt8BNfyJjElogdzR9Kh/eNxvm4HoluF0lOxLDRLujPe2jUZYecLBD+FbKeV7FiC4T+W6iho428UjyX4okD7eJ+FKvvo5LrKcALVlgexSf1Q/ERg3vAYLDzJB+1zDgppoSrOOrsaQKBgCcH5ZU6qQpzOh0QdmSdf73i1uVAgW1djwdtumcylc6zNr8n/HYi67zuGB7pVaeQX3VvML+et6ZZdkMK2mckcv1n8jbRFJJqzVI9vAEqu2uGfz3C5VsFtUTB1NmmBrTJcqtwk05Ud9Dy8+80MCTcH7LBBklMapll1QQWdvOYaSu7";
            String publicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy0Jy1rbycbPgGCb5ehx7LVc5pmFAY63g31ujf6xIyXX7yl4leShdI1P1hEPwG62K0TaY5++6AyKrsaCP9CPA80L8Phn/owzY4lOps8Ck+KI+0Gp01KmIpvQRcu37TKL9Ch/6skweSMUZolOCBGKseANaIaGMY5l5bWVkoP8wGT87+2qsvyHpT1G2MUInm6BiyNqsVYCMYnCXeqGWkchfpZ8+Z4EryFX4M7Z0cafYGTLPC6LmoRq/Ka6pRzYLkrP2JwBEt4KgZ2SllI9cIq9ad8efhzx/kCQ8ky347QZhy9gWPdD38OlNi4iAwVpP5Smkdu1EbldMSba67ERAIzecZQIDAQAB";
            String sign = RSAUtil.Signer.sign(json, privateKey, RSAUtil.SignAlgorithm.SHA256withRSA);
            // Set headers
            httpPost.setHeader("sign", sign);
            httpPost.setHeader("appInstanceId", head.getAppInstanceId());
            httpPost.setHeader("apiVersion", head.getApiVersion());
            httpPost.setHeader("nonce", head.getNonce());
            httpPost.setHeader("timestamp", head.getTimestamp());
            httpPost.setHeader("Content-Type", "application/json");

            // Set payload
            if (json == null) {
                httpPost.setEntity(null);
            } else {
                StringEntity entity = new StringEntity(json);
                httpPost.setEntity(entity);
            }

            // Send request and get response
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // Print response status and body
                System.out.println("Response Status: " + response.getStatusLine());
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response Body: " + responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
