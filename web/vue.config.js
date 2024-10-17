const baseUrl = 'http://127.0.0.1:80'; // 提取 baseUrl
module.exports = {
    devServer: {
        proxy: {
            '/orders': {
                target: `${baseUrl}/orders/`,
                changeOrigin: true
            },
            '/qrcode': {
                target: `${baseUrl}/qrcode/`,
                changeOrigin: true
            },
            '/login': {
                target: `${baseUrl}/login/`,
                changeOrigin: true
            },
            '/buttonAction': {
                target: `${baseUrl}/buttonAction/`,
                changeOrigin: true
            }
        }
    }
};
