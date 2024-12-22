const baseUrl = 'http://127.0.0.1:8080'; // 提取 baseUrl
module.exports = {
    devServer: {
        proxy: {
            '/orders': {
                target: `${baseUrl}/`,
                changeOrigin: true,
                onProxyReq(proxyReq) {
                    proxyReq.removeHeader('origin')
                }
            },
            '/login': {
                target: `${baseUrl}/`,
                changeOrigin: true,
                onProxyReq(proxyReq) {
                    proxyReq.removeHeader('origin')
                }
            },
            '/setAccount': {
                target: `${baseUrl}/`,
                changeOrigin: true,
                onProxyReq(proxyReq) {
                    proxyReq.removeHeader('origin')
                }
            },
            '/buttonAction': {
                target: `${baseUrl}/`,
                changeOrigin: true,
                onProxyReq(proxyReq) {
                    proxyReq.removeHeader('origin')
                }
            }
        }
    }
};
