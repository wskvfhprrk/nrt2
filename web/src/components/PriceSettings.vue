<template>
  <div>
    <el-form label-width="50px">
      <!-- Extra Portion Price -->
      <el-form-item label="加量">
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
      <el-button type="primary" @click="submitSettings">提交设置</el-button>
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
.el-form-item {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}
span {
  margin-left: 5px;
}
</style>
