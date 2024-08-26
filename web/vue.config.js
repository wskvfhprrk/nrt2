module.exports = {
    devServer: {
        proxy: {
            '/orders': {
                target: 'http://127.0.0.1:8080',
                changeOrigin: true
            },
            '/buttonAction': {
                target: 'http://127.0.0.1:8080',
                changeOrigin: true
            }
        }
    }
};
