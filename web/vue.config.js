const baseUrl = 'http://127.0.0.1:8080'; // 提取 baseUrl
module.exports = {
    devServer: {
        proxy: {
            '/orders': {
                target: `${baseUrl}/`,
                changeOrigin: true,
                // 后面新增的配置
                onProxyReq(proxyReq) {
                    proxyReq.removeHeader('origin')
                }
            },
            '/login': {
                target: `${baseUrl}/`,
                changeOrigin: true,
                secure: false,
                // 后面新增的配置
                onProxyReq(proxyReq) {
                    proxyReq.removeHeader('origin')
                }
            },
            '/buttonAction': {
                target: `${baseUrl}/`,
                changeOrigin: true,
                secure: false,
            }
        }
    }
};
