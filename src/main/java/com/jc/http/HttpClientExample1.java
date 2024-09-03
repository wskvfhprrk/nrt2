package com.jc.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientExample1 {
    public static void main(String[] args) {
        Head head = new Head();
        head.setUrl("/opencenter/order/query/detailByPickUpGoodsNo");
        head.setNonce("345tgyve5r67");
        head.setTimestamp("1689651147");
        head.setDataJson( "{\"partnerId\":2725,\"appId\":\"1231321\",\"authMode\":\"mobile\",\"authCode\":15216868575}");
        sendHttp(head);
    }

    public static void sendHttp(Head head) {
        String url = "https://open-center-platform-gateway.sandload.cn" + head.getUrl();
        String headDataJson = head.getDataJson();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
//            String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCadOMGuXlltrpZcg4/AeHRIUqcGRT1QjZS6qmeoG/8dNO1VW7zoGZnh9/6oH0lV3D+b5H/ASm857hVHIyH3GaxlTAygvmLFE+KUx7wsClNUfxMtKLRAY/N/mFO3yuc+5twkh7aDC0MtOTsYMQNgsKqPgR0l6z2bQ+zEqyiPZUFQdhRh9SzBg2yL/jXk70p89OdzDQkZ83yWr58ML+P9zjEnlQkdH1xBmBGpAD2mYr7HqdvfzP9V2OlY3eYVQ+ZOqy4UDwabCJh4U83Gmo90fbVsYJexvdcROZn9riVhzEhhhPhWDs4/B8Zl+WPV8UDm447YPjThPjwZxaUx1VQAaZvAgMBAAECggEAGb1X5hN7pRUt+eD1ec+M+8ZyAhB1+ydFGU1M5gfzkZ1AeKwbmNMGrcMpcsFTCamIRYZ/TIE7nOT6xYhD7RIwNPDZPtmvz1sbpEAU3GRnBaEfjALgTTdW6Su2uKedQ4R0k25uYMT9ruvoaHH5ygyb9zXtlSZ/pvNm1d9LUKA1mNHtaLY7Ix7crUQ0geQOiY3nZtr7AHkcBxSIKTiuBoeorOE7iSDRT1RkK5okhEkpGUFW2xmIEjb6T8y3g7iUVi/45IB5vCjWtOTG4gm7p3LACg4VpDgUflhK/S1Ksj96GXsCy7PsyUVOa93SQcNBIstp3amzuNKFILIziK+DQzUqzQKBgQC9PDwFR7rc41BARQ/VuN1LScMwVgh2KHedeiuLG1jR79IOY/ok8181+8NX+ck1Ey07gpBzo88+5Nja4z4TgW2uycYD0WuFbL/j+6E9S2/k2t0LAeSbGnUMmkNDSwE359Qd3r6HqvO25X17GTfn2WL9zShIN7m3ti1SqIWDS9YaRQKBgQDQ828LSdJHt7rL63fzi8CdPg0exmN05YuHKktC48FVjhfkU6ZAEBn2bn/Bn+q7UjizNT9nh6AcP6TqipdCQR5ErKE1fh2fjc6xT4AG+0HjLLgWaGlYrCoKRDUDmC0OtYmvQGIRGWqpvNuzAZqcISYE9fL+ZMZMZDvwmihLoSxDIwKBgGz7NpnX4I77vqj03RS5vYBxf57jvUXHMnQX+uHHIxTsp62v+Ey1eHzPfIW7Dhyp6Z/fJq/ihCV1iEovGv4CyRcjjw192w1hPvXeYdK6Ejvbq/d95JlK2GQkp14m4RHASpHVSSc61dbcPbLaKC83K+J895j3ttZNbbav5QxjbW99AoGBAIwuxZENNcZnvSqSDyeV2uOjfann71G9rp5AAb4ejtaXPfAxLphMNR7fg1ajsxd5UhDo6GDM3N4W1M2FrXUFuwjXyE/bDIS+3qyWaY5Fgy6mEdr2sbaLyqiYmfiPX6s+n+CzkRdnteeqjLrLmK6mjptFZoDFm0Qk+xGwWthiiQ+BAoGBAK2A6FKk0ohh6syxgZe0OBbE/3uue8fC7yJn8cG5V87EKZnPTx/jpYIKm82al43bFncqQSv05kBMMDsQpLejSwuzUJCeSWZVYieUn76TioDslkZI7n+kjSiluxMTkbV4pvNsQDxG1iILIDtSBusQp8GLp3bp85ErbWEnVllmb8wG";
            String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmnTjBrl5Zba6WXIOPwHh0SFKnBkU9UI2UuqpnqBv/HTTtVVu86BmZ4ff+qB9JVdw/m+R/wEpvOe4VRyMh9xmsZUwMoL5ixRPilMe8LApTVH8TLSi0QGPzf5hTt8rnPubcJIe2gwtDLTk7GDEDYLCqj4EdJes9m0PsxKsoj2VBUHYUYfUswYNsi/415O9KfPTncw0JGfN8lq+fDC/j/c4xJ5UJHR9cQZgRqQA9pmK+x6nb38z/VdjpWN3mFUPmTqsuFA8GmwiYeFPNxpqPdH21bGCXsb3XETmZ/a4lYcxIYYT4Vg7OPwfGZflj1fFA5uOO2D404T48GcWlMdVUAGmbwIDAQAB";
            String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCHer9bJenBGT813uR2FD62X9URkQIRcREkIEV6PYLEJ5XvkOrhQYHg1vRa/huhCmHkUR9Vjq/cWyv27AauO7qJBeuoGErhf7aBMq1Vjf5uwaBNfMKy28mxTdnIosEL0z6XmFgt3h3tk2TrB0hzVDDSfZdyE5dZ3dpSJwkd2WREf3Tom9KKzXEIBrBKIMuQ0Iu+Fp9iz84guPnYNEcSAJf1+AkjZKagChdQxS0wgfp5lQFsusarzXVg1/HpTSNlIKSYgJCc+C+MexfCoVCYtNmkheii1ZBEqiF211YqczT5g+F/UHdiRg2Ec5X9yXoiCNr9do1HIZ7G4lZQSIZBxCQ9AgMBAAECggEAUrt1oZ68aYwWWrpbf3QLe+l3vvtzbN1EH3CJnQV1Fn4qBJrVpGsRehEqNWrZynUBpKCZZvD0mbcfWPF3fuzAk66G4ya6i4wnEiy1RvqoNlCNqOQYLYskVt74sJobEzKUFZUVvCTY19zbkt7msm7mRZQmaKZoFZvyF5r46T55CY5xV+SXKFYggJVvq+hIFLpu7M3VTvnYk8zRvOC5hrGeABgTQzOX5DnNYeudyo813mQD0+YFDZ1iZYO7zvltXDtisZqATUTmeXdY5o8JJc2Z8kZkLU+Zat5b7m9u/8yT38Tug+K+axLLgG6fRzzcWOd71s8C9hphAMj0SLgGEjbqwQKBgQD+lYjvfSjGLWmaMv7R9a/xE5J5bYbb7q4tfFfHSqkKxrwVngi+I67H8YdCXJcAUnbjpWqiZEwudrtclsaBNpZrjC7LwFHRNQ0C0cc0NsDoMV5crWm9Hf9wPjKTUf5Dk+j1eEk3U9Z+NB73ga7sccMfUgEb7Fs9BSmD5Qf6dxfcrQKBgQCIO6MMN1WwaHjrBQLmdFDOGZbLNCz9PQQq/H2nTqfYF03KCXT9IP7S0YLwXBo1DJqzywy3C9Pyd3hffxpEEFSJS0fCRC9DwtmWetWYwozZn4T5Ic8VTvrWSoa54eG7lGkfT3zh7LEviRFmDFCr9X94h1to8TjFt0uzgY4lIwlH0QKBgDaRGJO0cOvhSfQ6H/Iixf5XIpsSRciYJQ8syqe1sfvUUvYTNG6EuW6zrzkjHV59TmxGxU9fThDbdBdJXezOJXmRx2n2o9LCmzJSgm8HmVtrUX1t0e8uIdUOD8sR1PzpFBKhIJhSSPQt+cE1M6gMTjVFX2V7yKaQl7Bkg8xEgIHVAoGATKtbeJ2PsQcqvgGE/1OIwmTuU5Aana6AMP0Gbmk61sdOtRybZXXzU4wrp7/908szKWplzoJulq+b5AqxKosSOG5QhUlAXF9Fe4XHvgAHqtY2zMq1M6XOT1mD9wrjsmC0xLVEcBV4Jmt6ijb2E8DHK3rp++Z97+/XP7/y0n2kVHECgYBEn/xYM6cLL8bWRAo6hrncJmxYSk1ceqYws0wyQP0PymPdnKY+drjtnfV/0C0m8s7NtW/B88uSxn4Bh6ZfbEJMu0Dh2xknZHL5Db+ioTEIkTjXt/p9Gl8hRbU/BmZnIBbV2QOnBYav/FvLjmLl12vFNjH+dweJtB7Dy0D2CN+4Uw==";
//            String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCwn8V3xwtnGEZ1zHAJH78CmqldsIrGT0li/iKpE7M4pdCeZyDrZqObR05fumqUCCdl+RY1OtdsnsWhctOIa0Jpe3FWuSNtabZmpMRuZlx0zSqjGZSmQkH931iWdqOON4rEDlfn/PR+/5jU16D4s4rg2+xig//pKdNXBRjzFI8Q57Kt8e41tKw8uYhgMbfBYEXKEF3GFnKhvQ0Abj9QQYacjh3gFedYPUVNxTBLiBVrmBW2xi68yUcU7iP/jp/pVCEp9V+suspgiT61IRSGq2O8inxSqotOzFNfmfdXzIUfit+w5USywmttalaJnAUoxoSoWwW0ZDg468t4c4oLvv5tAgMBAAECggEAK19YsQSmdq8d4mCNJJhwofWT4KdxNKiUt2P74/Qr/x2s4LEngtcGFyQICXbwJCDqd96WhwTHkOteUbquWmIogVmoZ+j3267pdURB3nBxhNc7AKlP+RHtPagCi5RfrpVSFLoG+TeDXMpsX9wBsgD4+iXXYwvSJJI6TTS5vzuNtFMgc/74nzPO9fI7563EN9/1Vl/dT9zUn4V/tK1SnUaGJY2GK/ERLN+cHmfCkekk6pRW9gRqXXWQoaUjl9jq0aRgmqz7uN2+cyfP01fUOKzBs+ZWiHbyh1nP8wdQVlELf+Lcs2jcPZ+kMcklnOCDmO6wWEZn0En3rVwnoxup/2NACwKBgQDrQ7cTPLdk1vZRt1nVgWwJes7qzyXQ2lCEaTzZDY05X2TN+Rbi8GfBUVDi3p5Z0MJhjaXrNui7wJl73M1IPQdUyuQy7ND8vmr2EKenE27XUEHM4oBWrsZpkBv/UqOjDg6gN2rqPrziaKDfQDWeRI2ejkDFNMyS+q3Z48v12WZsSwKBgQDAMPMpEsy2TCAQtvVEBzX30xDwBazynO0ldosWMN7MckGhYv7nbIWnhbG6VpePTt/kPfE+keVg6PImDFWHo8le05/i73fK3fbh32Aqh3ZxtGHHnK5YcWj4O5eW2ZOG3comvBh9pdYeha8sWSdnCRcZk+YXYjERx5YHargGbKAdJwKBgQC2Unr9oI8ryd+IgoRPDwaP7imPFUvkKFw/0WLDf1oTvVbcvQ48fzyZN5Bs0UN8rWkGuwR7XclNP7xynjJEqm8VCkuBH7/yLwureErFEy1SFbN4uYvmIXpIctMPspZ4FbvF9EyaDlbJS3ZorVUgO1ZZKzsM2Zc0zKqQcpYtB/b1MwKBgCmUrZcuVRH/yFQ1cT0Fnlx+0sRLRg67eudHDXKAokNzWNW02FdzeDs1Rq18/I9pzjDQYfhaklj0LSekNYECI5gbixULIEzI1xgpPuIUqsCQ+TK5qFLEPqSuJ+7gJfXg2FKnEVll3rx9ydd7dnXpVpEXGYnnmZwyJTh3k1b4e2+zAoGARy8xrUC/ngudEmitsDR4EtjEcvXgfN8+M7DMC5/MoxzumGZ28rqe9eGhji5Cm05WEy9Jls4h6d0o6gze2eLmk7VZw6BJd1d6kVgzGgW4456yt8BhSvcWG47qMXmz80A3B7WUXFw22U3SRExg8XEEkhPKlXQFSSGnRA1rRcyj6WQ=";
//            String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzTUaY43TqMpQYccneutQRkSTJ6lXK/renSfCHC2188jya9PVMNcctRcdpsoZ19RdwAHypctKc5wmEUMlaR8XUnopz9h0c6IiRDVD9THz37VB6A4sGGOsRYYvt3zrSNPUHDv3nsaBvkFxeWkitziQd7zekdKzN/wvpIMcrJPFw/jgX98b0lqj4tVP2tIgnByWwjxQoYiR2qWRpufVk18iJJ6cHgiCx0PKI3h4qfma6yx9QQHKHfgrUIGyt6SmHdjI27XYQ7o6qBzQPZx/Q8YFKBmOUqKYGoQM1+RfsVoEwPXu9kaAny/NFmX2FxLLzQvMytt7w6uQoLKq0Px0wOouzQIDAQAB";
            String content = head.getTimestamp() + ";" + head.getNonce() + ";" + headDataJson + ";";
            String sign = RSAUtil.Signer.sign(content, privateKey, RSAUtil.SignAlgorithm.SHA256withRSA);
            System.out.println("sing=="+sign);
            boolean verify = RSAUtil.Signer.verify(content, sign, publicKey, RSAUtil.SignAlgorithm.SHA256withRSA);
//            System.out.println("签名验证结果：" + verify);

            httpPost.setHeader("sign", sign);
            httpPost.setHeader("appInstanceId", head.getAppInstanceId());
            httpPost.setHeader("apiVersion", head.getApiVersion());
            httpPost.setHeader("nonce", head.getNonce());
            httpPost.setHeader("timestamp", head.getTimestamp());
            httpPost.setHeader("Content-Type", "application/json");
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
