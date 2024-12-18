import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import './assets/styles/global.css';
// import 'element-ui/lib/theme-chalk/index.css';
createApp(App).use(router).use(ElementPlus).mount('#app');
