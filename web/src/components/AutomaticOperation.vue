<template>
  <el-form :model="form" label-width="300px">
    <el-row gutter="10">
      <!-- Existing fields -->
      <el-col :span="24">
        <el-form-item label="汤自动循环排空气" style="font-size: 15px;">
          <el-input v-model="form.circulateAir" placeholder="Enter value">
            <template #append>分钟/次</template>
          </el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="汤保温自动检测">
          <el-input v-model="form.autoCheckTemp" placeholder="Enter value">
            <template #append>分钟/次</template>
          </el-input>
        </el-form-item>
      </el-col>

      <!-- New fields -->
      <el-col :span="24">
        <el-form-item label="汤保温最高温度">
          <el-input v-model="form.maxTemp" placeholder="Enter value">
            <template #append>度</template>
          </el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="汤保温最低温度">
          <el-input v-model="form.minTemp" placeholder="Enter value">
            <template #append>度</template>
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <!-- Submit button -->
    <el-form-item>
      <el-button type="primary" @click="submitForm">修改</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  data() {
    return {
      form: {
        circulateAir: 20,
        autoCheckTemp: 10,
        maxTemp: 100,
        minTemp: 80
      }
    };
  },
  methods: {
    submitForm() {
      console.log("Form submitted:", this.form);
      // Add your submit functionality here
    }
  }
};
</script>

<style scoped>
.el-form {

  width: 400px;
  margin: 100px auto;
  padding-top: 20px;
}
.el-button {
  width: 100%;
}
</style>
