<!-- components/SetAccount.vue -->
<template>
  <el-main>
    <h1>账户设置</h1>
    <div class="form">
      <el-form :model="form" status-icon :rules="rules" ref="form" label-width="100px" class="demo-form">
        <el-form-item label="本机机器码" prop="code" >
          <el-input v-model.number="form.code" readonly></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('form')">提交</el-button>
          <el-button @click="resetForm('form')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-main>
</template>

<script>
import axios from "axios";
const baseUrl = 'http://127.0.0.1:8080/setAccount';
export default {
  name: 'SetAccount',
  data() {
    return{
      form: {
        code: '',
        password: ''
      },
    }
  },
  methods: {
    async submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          try {
            const response = await axios.post(baseUrl + "/getKey", this.form, {
              headers: {
                'Content-Type': 'application/json'
              }
            });
            this.orderSubmitted = true;
            this.$message.success('密钥已经更新！');
          } catch (error) {
            this.$message.error('订单提交失败');
            console.error(error);
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    async getCode(){
      try {
        const response = await axios.get(baseUrl + '/getCode');
        if (response.data.code === 200) {
          this.form.code = response.data.data;
        } else {
          console.error(response.data.message);
        }
      } catch (error) {
        console.error('获取订单数据时出错:', error);
      }
    }
  },
  mounted() {
    this.getCode();
  }
}
</script>

<style scoped>
.form{
  margin-top: 100px;
  width: 350px;
}
</style>
