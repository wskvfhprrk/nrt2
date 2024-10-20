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
    <!-- 二维码方框 -->
    <div class="qr-code-container" v-show="qrCodeVisible">
      <div class="qr-title">
        <el-avatar :size="25" src="https://empty" @error="errorHandler">
          <img src='../assets/img/wechat-logo.png'/>
        </el-avatar>
        微信支付
      </div>
      <div class="orderId">订单：<span style="color:red;margin-bottom: 10px;">{{ orderId }}</span></div>
      <div id="qrcode" style="width: 120px; height: 120px;"></div>
    </div>
    <div class="container">
      <el-container>
        <!-- <el-header class="center-content"> 牛肉汤自助点餐系统 </el-header> -->
        <el-main>
          <el-form :model="form" label-width="220px">
            <el-form-item label="选择食谱">
              <el-radio-group v-model="form.selectedRecipe">
                <el-radio-button :value="'牛肉汤'">牛肉汤</el-radio-button>
                <el-radio-button :value="'牛杂汤'">牛杂汤</el-radio-button>
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
                <el-radio-button :value="true">是</el-radio-button>
                <el-radio-button :value="false">否</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="添加葱花">
              <el-radio-group v-model="form.addOnion">
                <el-radio-button :value="true">是</el-radio-button>
                <el-radio-button :value="false">否</el-radio-button>
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
            <el-button type="primary" @click="submitOrder" class="center-button">提交订单
            </el-button>
            <!--            <el-button type="primary" :disabled="!isButtonEnabled" @click="submitOrder" class="center-button">提交订单
                        </el-button>-->
          </div>
        </el-main>
      </el-container>
    </div>
    <!-- Status message section -->
    <div class="status-message" :style="{ color: serverStatus.color }">
      {{ serverStatus.message || '默认状态信息显示' }}
    </div>

    <div class="go-to-backend">
      <el-button type="primary" @click="goToBackend">进入后台</el-button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import QRCode from 'qrcodejs2';

const baseUrl = 'http://127.0.0.1:8080/orders';
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
        {key: 'wechat', label: '微信支付', colorDark: "#000000", colorLight: "#07C160"},
        {key: 'alipay', label: '支付宝支付（开发中）', colorDark: "#FFFFFF", colorLight: "#10AEFF"}
      ],
      serverStatus: {
        color: 'black',
        message: ''
      },
      pendingOrders: [],       // 待做订单队列
      inProgressOrders: [],    // 在做订单队列
      completedOrders: [],     // 做完订单队列
      hasOrders: false,         // 是否有订单

      errorHandler: null, // 确保 errorHandler 定义在 data 中

      qrCodeDialogTitle: "微信支付", // 默认微信支付
      qrCodeVisible: false,    // 控制二维码弹窗的显示与隐藏
      qrCodeText: '',               // 用于生成二维码的url
      orderId: 'A0000',
      payMethods: ''    //付款方式——正在支付的订单
    };
  },
  methods: {
    // 生成二维码的方法
    generateQrCode() {
      // 清空之前的二维码（避免多次生成重叠）
      const qrcodeElement = document.getElementById('qrcode');
      if (qrcodeElement) {
        qrcodeElement.innerHTML = ''; // 清除已有二维码
        // 生成新的二维码
        new QRCode(qrcodeElement, {
          text: this.qrCodeText,
          width: 120, // QR码宽度
          height: 120, // QR码高度
          colorDark: "#000000", // QR码颜色
          colorLight: "#ffffff", // 背景色
          correctLevel: QRCode.CorrectLevel.H // 纠错级别（L, M, Q, H）
        });
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
          const response = await axios.post(baseUrl, this.form, {
            headers: {
              'Content-Type': 'application/json'
            }
          });
          this.orderSubmitted = true;
          // this.$message.success('订单提交成功');
          // setTimeout(() => {
          //   location.reload();
          // }, 5000); // 5秒后刷新页面
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
        const response = await axios.get(baseUrl + '/serverStatus');
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
        const response = await axios.get(baseUrl + '/status');
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
    },
    //查询二给码信息
    async fetchQrData() {
      try {
        const response = await axios.get(baseUrl + '/qrcode');
        if (response.data.code === 200 & response.data.data !== null) {
          this.qrCodeText = response.data.data.qrCodeText;
          console.log("response.data.data.qrCodeText===" + response.data.data)
          this.orderId = response.data.data.orderId;
          this.payMethods = response.data.data.payMethods;
          this.generateQrCode();
          this.qrCodeVisible = true;
        } else {
          this.qrCodeVisible = false;
          console.error(response.data.message);
        }
      } catch (error) {
        this.qrCodeVisible = false;
        console.error('获取订单数据时出错:', error);
      }
    },
    //websocket连接
    connectWebSocket() {
      this.ws = new WebSocket("ws://localhost:8080/ws");
      console.log("连接websocket……")
      this.ws.onmessage = (event) => {
        // console.log(event.data)
        if (event.data === 'qrSuccess') {
          this.fetchQrData();
        }
        if (event.data === 'paySuccess') {
          this.qrCodeVisible = false;
          //更新订单
          this.fetchOrderData();
          this.$message.success('根据订单编号取餐！')
        }
      };

      this.ws.onopen = () => {
        console.log("WebSocket连接成功");
      };
      this.ws.onclose = () => {
        console.log("WebSocket连接关闭");
      };
    }
  },
  mounted() {
    //启动websocket
    this.connectWebSocket();
    /*清空登陆session*/
    this.cleanSession();
    this.fetchServerStatus();
    this.fetchOrderData();
    setInterval(this.fetchOrderData, 1000);

    this.generateQrCode(); // 页面加载时生成二维码
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
  margin-top: 50px;
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
  bottom: 100px;
  right: 0px;
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

.qr-code-container {
  position: fixed;
  bottom: 20px;
  left: 20px;
  width: 150px;
  height: 200px;
  background-color: white;
  border: 3px solid #07C160;
  border-radius: 10px;
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin-bottom: 130px;
}

.qr-title {
  display: flex;
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  font-size: 20px;
  height: 25px;
  color: #07C160;
  margin-bottom: 5px;
}


</style>
