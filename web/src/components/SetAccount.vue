<!-- components/SetAccount.vue -->
<template>
  <el-main>
    <div class="form">
      <el-form :model="form" status-icon :rules="rules" ref="form" label-width="250px" class="demo-form">
        <el-form-item label="本机机器码" prop="code" >
          <el-input v-model.number="form.code" readonly></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('form')">重密密钥</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-main>
</template>

<script>
import axios from "axios";

export default {
  name: 'SetAccount',
  data() {
    return {
      form: {
        code: '',
        password: ''
      },
      rules: {
        code: [
          {required: true, message: '请输入本机编号', trigger: 'blur'},
          {min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    async submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          try {
            const response = await axios.post( "setAccount/getKey", this.form, {
              headers: {
                'Content-Type': 'application/json'
              }
            });
            this.orderSubmitted = true;
          } catch (error) {
            this.$message.error('订单提交失败');
            console.error(error);
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }
    ,
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
    ,
    async getCode() {
      try {
        const response = await axios.get( 'setAccount/getCode');
        if (response.data.code === 200) {
          this.form.code = response.data.data;
        } else {
          console.error(response.data.message);
        }
      } catch (error) {
        console.error('获取订单数据时出错:', error);
      }
    }
  }
  ,
  mounted() {
    this.getCode();
  }
}
</script>

<style scoped>
.el-form-item label {
  font-size: 15px;
}
.form {
  margin-top: 100px;
  width: 350px;
}
</style>
