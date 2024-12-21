const baseUrl = 'http://127.0.0.1:8080'; // 提取 baseUrl
module.exports = {
    devServer: {
        proxy: {
            '/orders': {
                target: `${baseUrl}/orders/`,
                changeOrigin: true,
                secure: false,
            },
            '/qrcode': {
                target: `${baseUrl}/qrcode/`,
                changeOrigin: true,
                secure: false,
            },
            '/login': {
                target: `${baseUrl}/login/`,
                changeOrigin: true,
                secure: false,
            },
            '/buttonAction': {
                target: `${baseUrl}/buttonAction/`,
                changeOrigin: true,
                secure: false,
            }
        }
    }
};
