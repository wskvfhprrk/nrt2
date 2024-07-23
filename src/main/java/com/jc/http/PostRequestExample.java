import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequestExample {
    public static void main(String[] args) {
        try {
            // 设置请求URL
            URL url = new URL("https://open-center-platform-gateway.sandload.cn/opencenter/order/query/detailByPickUpGoodsNo");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设置代理（如果需要）
            System.setProperty("http.proxyHost", "127.0.0.1");
            System.setProperty("http.proxyPort", "7890");

            // 设置请求方法和头信息
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "PostmanRuntime/7.40.0");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Postman-Token", "6c41d85e-b55d-48a7-964c-4bd53895726c");
            conn.setRequestProperty("Host", "open-center-platform-gateway.sandload.cn");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            conn.setRequestProperty("Connection", "keep-alive");

            // 启用输出流
            conn.setDoOutput(true);

            // 设置请求体
            String jsonInputString = "{\"key\":\"value\"}";  // 替换为实际的JSON请求体
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 获取响应码
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 读取响应
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            String response = new String(baos.toByteArray(), "utf-8");
            System.out.println("Response: " + response);

            // 关闭连接
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
