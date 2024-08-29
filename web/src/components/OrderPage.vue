<template>
  <div id="app" class="outer-container">
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
            <el-button type="primary"  :disabled="!isButtonEnabled"  @click="submitOrder" class="center-button">提交订单</el-button>
          </div>
          <div v-if="orderSubmitted" class="order-details">
            <h2>订单详情</h2>
            <p>食谱: {{ form.selectedRecipe }}</p>
            <p>类别: {{ form.selectedPrice }}元</p>
            <p>口味: {{ form.selectedSpice }}</p>
            <p>加香菜: {{ form.addCilantro ? '是' : '否' }}</p>
            <p>加葱: {{ form.addOnion ? '是' : '否' }}</p>
          </div>
        </el-main>
      </el-container>
    </div>
    <!-- Status message section -->
    <div class="status-message" :style="{ color: serverStatus.color }">
      {{ serverStatus.message }}
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
      isButtonEnabled:false,
      serverStatus: {
        color: 'black',
        message: ''
      }
    };
  },
  methods: {
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
        console.error('Error fetching server status:', error);
        // 更新按钮状态
        this.isButtonEnabled = false;
      }
    }
  },
  mounted() {
    this.fetchServerStatus(); // Initial fetch
    setInterval(this.fetchServerStatus, 1000); // Poll every second
  }
};
</script>

<style>
html, body {
  height: 100%;
  margin: 0;
  background-color: transparent; /* 设置为透明背景 */
}

.outer-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  flex-direction: column; /* 让提示信息在底部显示 */
}

.container {
  max-width: 800px;
  width: 100%;
  padding: 20px;
  font-family: 'Arial', sans-serif;
  background-color: transparent; /* 设置为透明背景 */
}

.center-content {
  font-size: calc(16px + 2vh); /* 根据屏幕高度调整字体大小 */
  text-align: center;
  color: rgb(72, 8, 25);
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

.el-header {
  color: rgb(72, 8, 25);
  padding: 10px;
}

.el-main {
  padding: 20px;
}

.el-form-item {
  margin-bottom: 40px; /* 原来的两倍 */
}

.el-form-item label {
  font-size: calc(8px + 1.5vh); /* 根据屏幕高度调整字体大小 */
  color: rgb(72, 8, 25);
}

.el-radio-group {
  font-size: calc(12px + 1.5vh); /* 根据屏幕高度调整字体大小 */
}

.el-radio {
  font-size: calc(12px + 1.5vh); /* 根据屏幕高度调整字体大小 */
}

.el-button {
  height: 40px;
  font-size: calc(12px + 1.5vh); /* 根据屏幕高度调整字体大小 */
}

.order-details {
  margin-top: 20px;
  font-size: calc(12px + 1.5vh); /* 根据屏幕高度调整字体大小 */
  color: rgb(72, 8, 25);
}

.order-details h2 {
  color: rgb(72, 8, 25);
  margin-bottom: 10px;
}

.status-message {
  width: 100%;
  padding: 30px;
  text-align: center;
  background-color: rgba(255, 255, 255, 0.8); /* 半透明白色背景 */
  font-size: calc(12px + 1.5vh); /* 根据屏幕高度调整字体大小 */
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 1000; /* 确保其在最前面 */
}
</style>
