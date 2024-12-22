<template>
  <div class="login-page">
    <div class="background"></div>
    <div class="login-box">
      <h2>售卖机后台管理</h2>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <input type="text" v-model="username" placeholder="请输入账号" required/>
        </div>
        <div class="form-group">
          <input type="password" v-model="password" placeholder="请输入密码" required/>
        </div>
        <button type="submit" class="login-button">登录</button>
      </form>
      <el-alert
          v-if="showAlert"
          type="error"
          center
          show-icon
      >
        {{ tex }}
      </el-alert>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {useRouter} from 'vue-router'; // 使用 Vue Router 进行跳转

export default {
  data() {
    return {
      username: '',
      password: '',
      showAlert: false, // 控制 alert 的显示状态
      tex: '' // alert 中显示的文本
    };
  },
  methods: {
    async submitForm() {
      try {
        // 发送 POST 请求到后端 API
        const url = `login`;
        const response = await axios.post(url, {
          username: this.username,
          password: this.password,
        });

        // 假设后端返回一个 token 表示登录成功
        if(response.data.code===200) {
          const token = response.data.data.token;

          // 保存 token 到 localStorage 或 Vuex，具体取决于你的应用
          localStorage.setItem('userToken', token);

          // 使用 Vue Router 跳转到 SalesStatistics 页面
          this.$router.push({name: 'SalesStatistics'});
          // this.$router.push({name: useRouter});

        }else {
          //弹出错误提示
          this.tex=response.data.message;
          this.showAlert=true;
          return;
        }

      } catch (error) {
        this.tex='登录失败，请检查账号或密码';
        this.showAlert=true;
      }
    }
  },
};
</script>

<style scoped>
.login-page {
  position: relative;
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('@/assets/img/graphic.png'); /* 替换为你的背景图片路径 */
  background-size: cover;
  background-position: center;
  z-index: 1;
}

.login-box {
  position: relative;
  z-index: 2;
  background: rgba(200, 200, 200, 0.7); /* 半透明的背景颜色 */
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 300px;
  margin-right: 50%; /* 使登录框靠左边*/
}

.login-box h2 {
  margin-top: 0;
  margin-bottom: 40px;
  font-weight: 600;
  font-size: 30px; /* 增大字体 */
  color: #333; /* 使用较深的灰色替代纯黑色，视觉更柔和 */
  text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.8); /* 添加阴影效果，提升立体感 */
}

.form-group {
  margin-bottom: 15px;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.remember-me input {
  margin-right: 10px;
}

.login-button {
  width: 80%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.login-button:hover {
  background-color: #0056b3;
}
.el-alert.is-center {
  margin-top: 10px;
}
</style>
