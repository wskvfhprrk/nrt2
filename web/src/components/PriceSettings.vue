<template>
  <div class="container">
    <el-form label-width="200px">
      <!-- Extra Portion Price -->
      <el-form-item label="加量" style="font-size: 15px;">
        <el-input v-model="extraPortion" style="width: 80px; background-color: #aef5ff; text-align: center;" />
        <span>元/碗</span>
      </el-form-item>

      <!-- Large Portion Price -->
      <el-form-item label="大份">
        <el-input v-model="largePortion" style="width: 80px; background-color: #aef5ff; text-align: center;" />
        <span>元/碗</span>
      </el-form-item>

      <!-- Medium Portion Price -->
      <el-form-item label="中份">
        <el-input v-model="mediumPortion" style="width: 80px; background-color: #aef5ff; text-align: center;" />
        <span>元/碗</span>
      </el-form-item>

      <!-- Small Portion Price -->
      <el-form-item label="小份">
        <el-input v-model="smallPortion" style="width: 80px; background-color: #aef5ff; text-align: center;" />
        <span>元/碗</span>
      </el-form-item>

      <!-- Submit Button -->
      <el-button type="primary" @click="submitSettings"  style="margin: 40px 40px;">提交设置</el-button>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      extraPortion: 30,
      largePortion: 20,
      mediumPortion: 15,
      smallPortion: 10
    };
  },
  methods: {
    submitSettings() {
      // Logic for submitting the settings can be added here
      console.log("Settings submitted", {
        extraPortion: this.extraPortion,
        largePortion: this.largePortion,
        mediumPortion: this.mediumPortion,
        smallPortion: this.smallPortion
      });
    }
  }
};
</script>

<style scoped>
.container {
  width: 600px;
  margin: 100px 100px;
}
.el-form-item {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}
span {
  margin-left: 5px;
}
::v-deep .el-form-item__label {
  font-size: 30px;
}
</style>
