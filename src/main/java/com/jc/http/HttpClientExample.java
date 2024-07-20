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
//            RSAUtil.Signer
            // Set headers
            httpPost.setHeader("sign", head.getSign());
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
