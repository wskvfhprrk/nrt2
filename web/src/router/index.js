import { createRouter, createWebHashHistory } from 'vue-router';
import OrderPage from '../components/OrderPage.vue';
import ButtonsPage from '../components/ButtonsPage.vue';

const routes = [
    { path: '/', name: 'OrderPage', component: OrderPage },
    { path: '/buttons', name: 'ButtonsPage', component: ButtonsPage }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

export default router;
