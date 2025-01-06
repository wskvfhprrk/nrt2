import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import './assets/styles/global.css';

// Configure Vue feature flags
const app = createApp(App);
app.config.globalProperties.__VUE_PROD_HYDRATION_MISMATCH_DETAILS__ = true;

app.use(router)
   .use(ElementPlus)
   .mount('#app');
