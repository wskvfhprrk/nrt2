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
        head.setUrl("/opencenter/order/query/detailByPickUpGoodsNo");
        head.setNonce(UUID.randomUUID().toString().replaceAll("-", ""));
        String timestamp = String.valueOf(System.currentTimeMillis());
        head.setTimestamp(timestamp);
        head.setDataJson("{\"partnerId\":\"7p92yl9q\",\"orderClient\":8}");
        sendHttp(head);
    }

    public static void sendHttp(Head head) {
        String url = "https://open-center-platform-gateway.sandload.cn" + head.getUrl();
        String headDataJson = head.getDataJson();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCadOMGuXlltrpZcg4/AeHRIUqcGRT1QjZS6qmeoG/8dNO1VW7zoGZnh9/6oH0lV3D+b5H/ASm857hVHIyH3GaxlTAygvmLFE+KUx7wsClNUfxMtKLRAY/N/mFO3yuc+5twkh7aDC0MtOTsYMQNgsKqPgR0l6z2bQ+zEqyiPZUFQdhRh9SzBg2yL/jXk70p89OdzDQkZ83yWr58ML+P9zjEnlQkdH1xBmBGpAD2mYr7HqdvfzP9V2OlY3eYVQ+ZOqy4UDwabCJh4U83Gmo90fbVsYJexvdcROZn9riVhzEhhhPhWDs4/B8Zl+WPV8UDm447YPjThPjwZxaUx1VQAaZvAgMBAAECggEAGb1X5hN7pRUt+eD1ec+M+8ZyAhB1+ydFGU1M5gfzkZ1AeKwbmNMGrcMpcsFTCamIRYZ/TIE7nOT6xYhD7RIwNPDZPtmvz1sbpEAU3GRnBaEfjALgTTdW6Su2uKedQ4R0k25uYMT9ruvoaHH5ygyb9zXtlSZ/pvNm1d9LUKA1mNHtaLY7Ix7crUQ0geQOiY3nZtr7AHkcBxSIKTiuBoeorOE7iSDRT1RkK5okhEkpGUFW2xmIEjb6T8y3g7iUVi/45IB5vCjWtOTG4gm7p3LACg4VpDgUflhK/S1Ksj96GXsCy7PsyUVOa93SQcNBIstp3amzuNKFILIziK+DQzUqzQKBgQC9PDwFR7rc41BARQ/VuN1LScMwVgh2KHedeiuLG1jR79IOY/ok8181+8NX+ck1Ey07gpBzo88+5Nja4z4TgW2uycYD0WuFbL/j+6E9S2/k2t0LAeSbGnUMmkNDSwE359Qd3r6HqvO25X17GTfn2WL9zShIN7m3ti1SqIWDS9YaRQKBgQDQ828LSdJHt7rL63fzi8CdPg0exmN05YuHKktC48FVjhfkU6ZAEBn2bn/Bn+q7UjizNT9nh6AcP6TqipdCQR5ErKE1fh2fjc6xT4AG+0HjLLgWaGlYrCoKRDUDmC0OtYmvQGIRGWqpvNuzAZqcISYE9fL+ZMZMZDvwmihLoSxDIwKBgGz7NpnX4I77vqj03RS5vYBxf57jvUXHMnQX+uHHIxTsp62v+Ey1eHzPfIW7Dhyp6Z/fJq/ihCV1iEovGv4CyRcjjw192w1hPvXeYdK6Ejvbq/d95JlK2GQkp14m4RHASpHVSSc61dbcPbLaKC83K+J895j3ttZNbbav5QxjbW99AoGBAIwuxZENNcZnvSqSDyeV2uOjfann71G9rp5AAb4ejtaXPfAxLphMNR7fg1ajsxd5UhDo6GDM3N4W1M2FrXUFuwjXyE/bDIS+3qyWaY5Fgy6mEdr2sbaLyqiYmfiPX6s+n+CzkRdnteeqjLrLmK6mjptFZoDFm0Qk+xGwWthiiQ+BAoGBAK2A6FKk0ohh6syxgZe0OBbE/3uue8fC7yJn8cG5V87EKZnPTx/jpYIKm82al43bFncqQSv05kBMMDsQpLejSwuzUJCeSWZVYieUn76TioDslkZI7n+kjSiluxMTkbV4pvNsQDxG1iILIDtSBusQp8GLp3bp85ErbWEnVllmb8wG";
            String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmnTjBrl5Zba6WXIOPwHh0SFKnBkU9UI2UuqpnqBv/HTTtVVu86BmZ4ff+qB9JVdw/m+R/wEpvOe4VRyMh9xmsZUwMoL5ixRPilMe8LApTVH8TLSi0QGPzf5hTt8rnPubcJIe2gwtDLTk7GDEDYLCqj4EdJes9m0PsxKsoj2VBUHYUYfUswYNsi/415O9KfPTncw0JGfN8lq+fDC/j/c4xJ5UJHR9cQZgRqQA9pmK+x6nb38z/VdjpWN3mFUPmTqsuFA8GmwiYeFPNxpqPdH21bGCXsb3XETmZ/a4lYcxIYYT4Vg7OPwfGZflj1fFA5uOO2D404T48GcWlMdVUAGmbwIDAQAB";
            String content = head.getTimestamp() + ";" + head.getNonce() + ";" + headDataJson + ";";
            String sign = RSAUtil.Signer.sign(content, privateKey, RSAUtil.SignAlgorithm.SHA256withRSA);
            boolean verify = RSAUtil.Signer.verify(content, sign, publicKey, RSAUtil.SignAlgorithm.SHA256withRSA);
            System.out.println("签名验证结果：" + verify);

            httpPost.setHeader("sign", sign);
            httpPost.setHeader("appInstanceId", head.getAppInstanceId());
            httpPost.setHeader("apiVersion", head.getApiVersion());
            httpPost.setHeader("nonce", head.getNonce());
            httpPost.setHeader("timestamp", head.getTimestamp());
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "*/*");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("User-Agent", "PostmanRuntime/7.40.0");
            httpPost.setHeader("Accept", "*/*");
            httpPost.setHeader("Postman-Token", "6c41d85e-b55d-48a7-964c-4bd53895726c");
            httpPost.setHeader("Host", "open-center-platform-gateway.sandload.cn");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.setHeader("Connection", "keep-alive");
            StringEntity entity = new StringEntity(headDataJson, "UTF-8");
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                System.out.println("Response Status: " + response.getStatusLine());
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println("Response Body: " + responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
