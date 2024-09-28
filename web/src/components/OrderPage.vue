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
          <div v-for="order in inProgressOrders" :key="order.orderId" class="customer-name">{{
              order.customerName
            }}
          </div>
        </div>
      </div>
      <div v-if="completedOrders.length" class="order-status completed">
        做完订单: <span>{{ completedOrders.length }}</span>
        <div class="order-list">
          <div v-for="order in completedOrders" :key="order.orderId" class="customer-name">{{
              order.customerName
            }}
          </div>
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
                <el-radio-button :label="'牛肉汤'">牛肉汤</el-radio-button>
                <el-radio-button :label="'牛杂汤'">牛杂汤</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="选择类别">
              <el-radio-group v-model="form.selectedPrice">
                <el-radio-button v-for="price in prices" :key="price" :label="price">{{ price }}元</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="选择口味">
              <el-radio-group v-model="form.selectedSpice">
                <el-radio-button v-for="spice in spices" :key="spice" :label="spice">{{ spice }}</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="添加香菜">
              <el-radio-group v-model="form.addCilantro">
                <el-radio-button :label="true">是</el-radio-button>
                <el-radio-button :label="false">否</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="添加葱花">
              <el-radio-group v-model="form.addOnion">
                <el-radio-button :label="true">是</el-radio-button>
                <el-radio-button :label="false">否</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="支付方式">
              <el-radio-group v-model="form.paymentMethod">
                <el-radio-button v-for="method in paymentMethods" :key="method.key" :label="method.key">
                  {{ method.label }}
                </el-radio-button>
              </el-radio-group>

            </el-form-item>
          </el-form>
          <div class="button-container">
            <el-button type="primary" :disabled="!isButtonEnabled" @click="submitOrder" class="center-button">提交订单
            </el-button>
          </div>
        </el-main>
      </el-container>
    </div>
    <!-- Status message section -->
    <div class="status-message" :style="{ color: serverStatus.color }">
      {{ serverStatus.message || '默认状态信息显示' }}
    </div>

    <div class="go-to-backend">
      <el-button type="primary" @click="openQrCodeDialog">后台登录</el-button>
    </div>
  </div>

  <!-- 二维码弹窗 -->
  <el-dialog :title="qrCodeDialogTitle" v-model="qrCodeDialogVisible" width="30%">
    <img :src="qrCodeImage" alt="二维码" style="width: 100%; height: auto;"/>
  </el-dialog>
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
        addOnion: true,
        paymentMethod: 'wechat'   // 默认选择微信支付
      },
      recipes: ['牛肉汤', '牛杂汤'],
      prices: [10, 15, 20],
      spices: ['不辣', '微辣', '中辣', '辣'],
      orderSubmitted: false,
      isButtonEnabled: false,
      paymentMethods: [
        { key: 'wechat', label: '微信支付' },
        { key: 'alipay', label: '支付宝支付' }
      ],
      serverStatus: {
        color: 'black',
        message: ''
      },
      pendingOrders: [],       // 待做订单队列
      inProgressOrders: [],    // 在做订单队列
      completedOrders: [],     // 做完订单队列
      hasOrders: false,         // 是否有订单

      qrCodeDialogTitle: "微信支付", // 默认微信支付
      qrCodeDialogVisible: false,    // 控制二维码弹窗的显示与隐藏
      qrCodeImage: ''               // 用于存储二维码图片的Base64字符串

    };
  },
  methods: {
    // 打开二维码弹窗并获取二维码图片
    async openQrCodeDialog() {
      try {
        // 发送请求获取二维码图片
        const response = await axios.get('http://localhost:8080/qrcode', {
          params: {
            text: 'wechatPayTest', // 替换为你想要生成二维码的文本
            paymentMethod: this.form.paymentMethod //支付方式
          },
          responseType: 'blob' // 将响应类型设为 blob，表示我们获取的是图片数据
        });

        // 将 blob 数据转换为 Base64 格式
        const blob = response.data;
        const reader = new FileReader();
        reader.readAsDataURL(blob);
        reader.onloadend = () => {
          this.qrCodeImage = reader.result; // 将 Base64 格式的图片数据存储
          this.qrCodeDialogVisible = true;  // 显示弹窗
        };

      } catch (error) {
        console.error('获取二维码失败:', error);
        this.$message.error('二维码加载失败');
      }
    },
    /*userToken*/
    cleanSession() {
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
  background-color: var(--beige);
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
}

.button-container {
  text-align: center;
  margin-top: 20px;
}

.center-button {
  width: 50%;
  background-color: var(--deep-blue);
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
  background-color: var(--gray);
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
  padding: 10px 0;
  background-color: var(--gray);
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
  color: var(--deep-blue);
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
  background-color: var(--deep-blue);
  color: var(--silver);
  margin-bottom: 50px;
}

.el-dialog__header {
  text-align: center;
}

.el-dialog__body img {
  display: block;
  margin: 0 auto;
  width: 100%;
  height: 100%;
}

</style>
