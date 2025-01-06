<template>
  <el-form :model="form" label-width="200px">
    <el-row gutter="10">
      <el-col :span="24" v-for="(field, index) in formFields" :key="index">
        <el-form-item :label="field.label" style="font-size: 15px;">
          <el-input v-model="form[field.model]" placeholder="Enter value">
            <template #append>{{ field.unit }}</template>
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
        addMeat: 30,
        largeMeat: 20,
        mediumMeat: 15,
        smallMeat: 10,
        addMisc: 30,
        largeMisc: 20,
        mediumMisc: 15,
        smallMisc: 10,
        spiceWeight: 5,
        flowerWeight: 3,
        minSoupTemp: 80,
        maxSoupTemp: 100,
        serveSoupTemp: 120,
        steamTime: 12,
        addSoupTime: 8
      },
      formFields: [
        { label: '加肉', model: 'addMeat', unit: 'g' },
        { label: '大份肉', model: 'largeMeat', unit: 'g' },
        { label: '中份肉', model: 'mediumMeat', unit: 'g' },
        { label: '小份肉', model: 'smallMeat', unit: 'g' },
        { label: '加杂', model: 'addMisc', unit: 'g' },
        { label: '大份杂', model: 'largeMisc', unit: 'g' },
        { label: '中份杂', model: 'mediumMisc', unit: 'g' },
        { label: '小份杂', model: 'smallMisc', unit: 'g' },
        { label: '香菜重量', model: 'spiceWeight', unit: 'g' },
        { label: '葱花重量', model: 'flowerWeight', unit: 'g' },
       /* { label: '汤保温最小温', model: 'minSoupTemp', unit: '度' },
        { label: '汤保温最大温', model: 'maxSoupTemp', unit: '度' },*/
        { label: '出汤温度', model: 'serveSoupTemp', unit: '度' },
        { label: '碗加蒸气', model: 'steamTime', unit: '秒' },
        { label: '加汤量', model: 'addSoupTime', unit: '秒' }
      ]
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
  width: 350px;
  margin: auto;
}
::v-deep .el-form-item__label {
  font-size: 30px;
}
</style>
