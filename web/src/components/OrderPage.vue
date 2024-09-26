<template>
  <div id="app" class="outer-container">
    <!-- Order status display -->
    <div v-if="hasOrders" class="order-status-display">
      <div v-if="pendingOrders.length" class="order-status pending">
        待做订单: <span>{{ pendingOrders.length }}</span>
        <div class="order-list">
          <div v-for="order in pendingOrders" :key="order.orderId" class="customer-name">{{ order.customerName }}</div>
        </div>
      </div>
      <div v-if="inProgressOrders.length" class="order-status in-progress">
        在做订单: <span>{{ inProgressOrders.length }}</span>
        <div class="order-list">
          <div v-for="order in inProgressOrders" :key="order.orderId" class="customer-name">{{ order.customerName }}</div>
        </div>
      </div>
      <div v-if="completedOrders.length" class="order-status completed">
        做完订单: <span>{{ completedOrders.length }}</span>
        <div class="order-list">
          <div v-for="order in completedOrders" :key="order.orderId" class="customer-name">{{ order.customerName }}</div>
        </div>
      </div>
    </div>

    <div class="container">
      <el-container>
        <!-- <el-header class="center-content"> 牛肉汤自助点餐系统 </el-header> -->
        <el-main>
          <el-form :model="form" label-width="120px">
            <el-form-item label="选择食谱">
              <el-radio-group v-model="form.selectedRecipe">
                <el-radio :label="'牛肉汤'">牛肉汤</el-radio>
                <el-radio :label="'牛杂汤'">牛杂汤</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="选择类别">
              <el-radio-group v-model="form.selectedPrice">
                <el-radio v-for="price in prices" :key="price" :label="price">{{ price }}元</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="选择口味">
              <el-radio-group v-model="form.selectedSpice">
                <el-radio v-for="spice in spices" :key="spice" :label="spice">{{ spice }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="添加香菜">
              <el-radio-group v-model="form.addCilantro">
                <el-radio :label="true">是</el-radio>
                <el-radio :label="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="添加葱花">
              <el-radio-group v-model="form.addOnion">
                <el-radio :label="true">是</el-radio>
                <el-radio :label="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
          <div class="button-container">
            <el-button type="primary" :disabled="!isButtonEnabled" @click="submitOrder" class="center-button">提交订单
            </el-button>
          </div>
<!--          <div v-if="orderSubmitted" class="order-details">
            <h2>订单详情</h2>
            <p>食谱: {{ form.selectedRecipe }}</p>
            <p>类别: {{ form.selectedPrice }}元</p>
            <p>口味: {{ form.selectedSpice }}</p>
            <p>加香菜: {{ form.addCilantro ? '是' : '否' }}</p>
            <p>加葱: {{ form.addOnion ? '是' : '否' }}</p>
          </div>-->
        </el-main>
      </el-container>
    </div>
    <!-- Status message section -->
    <div class="status-message" :style="{ color: serverStatus.color }">
      {{ serverStatus.message || '默认状态信息显示' }}
    </div>

    <div class="go-to-backend">
      <el-button type="primary" @click="goToBackend">后台登录</el-button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'App',
  data() {
    return {
      form: {
        selectedRecipe: '牛肉汤',
        selectedPrice: 20,
        selectedSpice: '微辣',
        addCilantro: true,
        addOnion: true
      },
      recipes: ['牛肉汤', '牛杂汤'],
      prices: [10, 15, 20],
      spices: ['不辣', '微辣', '中辣', '辣'],
      orderSubmitted: false,
      isButtonEnabled: false,
      serverStatus: {
        color: 'black',
        message: ''
      },
      pendingOrders: [],       // 待做订单队列
      inProgressOrders: [],    // 在做订单队列
      completedOrders: [],     // 做完订单队列
      hasOrders: false         // 是否有订单
    };
  },
  methods: {
    /*userToken*/
    cleanSession(){
      localStorage.removeItem('userToken');
    },
    /*去登陆页面*/
    goToBackend() {
      this.$router.push('/login'); // 没有权限就返回到登陆页
    },
    async submitOrder() {
      if (this.form.selectedRecipe && this.form.selectedPrice && this.form.selectedSpice) {
        try {
          const response = await axios.post('http://127.0.0.1:8080/orders', this.form, {
            headers: {
              'Content-Type': 'application/json'
            }
          });
          this.orderSubmitted = true;
          this.$message.success('订单提交成功');
          console.log(response.data);
          setTimeout(() => {
            location.reload();
          }, 5000); // 5秒后刷新页面
        } catch (error) {
          this.$message.error('订单提交失败');
          console.error(error);
        }
      } else {
        this.$message.error('请完整选择所有选项');
      }
    },
    async fetchServerStatus() {
      try {
        const response = await axios.get('http://127.0.0.1:8080/orders/serverStatus');
        this.serverStatus.color = response.data.color;
        this.serverStatus.message = response.data.message;
        // 更新按钮状态
        this.isButtonEnabled = (this.serverStatus.color === 'green');
      } catch (error) {
        console.error('获取服务器状态时出错:', error);
        this.isButtonEnabled = false; // 如果请求失败，也禁用按钮
      }
    },
    async fetchOrderData() {
      try {
        const response = await axios.get('http://127.0.0.1:8080/orders/status');
        if (response.data.code === 200) {
          this.pendingOrders = response.data.data.pendingOrders || [];
          this.inProgressOrders = response.data.data.inProgressOrders || [];
          this.completedOrders = response.data.data.completedOrders || [];

          // 更新 hasOrders 状态
          this.hasOrders = this.pendingOrders.length > 0 || this.inProgressOrders.length > 0 || this.completedOrders.length > 0;
        } else {
          console.error(response.data.message);
        }
      } catch (error) {
        console.error('获取订单数据时出错:', error);
      }
    }
  },
  mounted() {
    /*清空登陆session*/
    this.cleanSession();
    this.fetchServerStatus(); // Initial fetch
    setInterval(this.fetchServerStatus, 1000); // Poll every second
    this.fetchOrderData(); // 获取订单数据
    setInterval(this.fetchOrderData, 1000); // 每5秒轮询一次订单数据
  }
};
</script>


<style>
html, body {
  height: 100%;
  margin: 0;
  background-color: transparent;
}

.outer-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  flex-direction: column;
}

.container {
  max-width: 600px;
  width: 100%;
  padding: 20px;
  font-family: 'Arial', sans-serif;
  background-color: transparent;
}

.button-container {
  text-align: center;
  margin-top: 20px;
}

.center-button {
  width: 50%;
  background-color: rgb(72, 8, 25);
  color: #fff;
}

.el-main {
  padding: 20px;
}

.el-form-item {
  margin-bottom: 40px;
}

.el-form-item label {
  font-size: calc(8px + 1.5vh);
  color: rgb(72, 8, 25);
}

.el-radio-group {
  font-size: calc(12px + 1.5vh);
}

.el-button {
  height: 40px;
  font-size: calc(12px + 1.5vh);
}

.order-details {
  margin-top: 20px;
  font-size: calc(12px + 1.5vh);
  color: rgb(72, 8, 25);
}

.status-message {
  width: 100%;
  padding: 20px;
  text-align: center;
  background-color: rgba(255, 255, 255, 0.8);
  font-size: calc(12px + 1.5vh);
  position: fixed;
  bottom: 0;
  left: 0;
}

.order-status-display {
  position: absolute;
  top: 0;
  width: 100%;
  text-align: center;
  background-color: rgba(255, 255, 255, 0.8);
  padding: 10px 0;
}

.order-status {
  font-size: calc(12px + 1.5vh);
  margin: 5px 0;
}

.order-status span {
  font-weight: bold;
  margin-left: 5px;
}

.order-list {
  display: grid;
  grid-template-columns: repeat(6, 1fr); /* 每行显不6个订单号 */
  gap: 5px;
  justify-content: center;
  max-width: 1000px; /* Set a max width to control the layout */
  margin: 0 auto;
}

.order-list .customer-name {
  text-align: center;
  font-size: calc(12px + 1.5vh);
}

.pending .customer-name {
  color: orange;
}

.in-progress .customer-name {
  color: blue;
}

.completed .customer-name {
  color: green;
}
/*后管管理按钮*/
.go-to-backend {
  position: fixed;
  bottom: 20px;
  right: 20px;
}

.go-to-backend .el-button {
  background-color: blue;
  color: #fff;
  margin-bottom: 20px;
}
</style>
