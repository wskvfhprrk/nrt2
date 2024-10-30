<template>
  <div style="text-align: center; padding-top: 50px;">
    <!-- 汤自动循环排空气 -->
    <div style="margin-bottom: 20px;">
      <span>汤自动循环排空气</span>
      <el-input-number v-model="airPurgeInterval" :min="1" :max="1440" :step="1" size="small" controls-position="right" style="width: 100px; margin: 0 10px;" />
      <span>分钟/次</span>
    </div>

    <!-- 汤保温自动检测 -->
    <div style="margin-bottom: 20px;">
      <span>汤保温自动检测</span>
      <el-input-number v-model="tempDetectionInterval" :min="1" :max="1440" :step="1" size="small" controls-position="right" style="width: 100px; margin: 0 10px;" />
      <span>分钟/次</span>
    </div>

    <!-- 修改按钮 -->
    <el-button type="primary" @click="updateSettings">修改</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      airPurgeInterval: 20, // 汤自动循环排空气的默认间隔时间
      tempDetectionInterval: 10, // 汤保温自动检测的默认间隔时间
    };
  },
  methods: {
    updateSettings() {
      // 修改设置的操作
      console.log(`汤自动循环排空气时间修改为: ${this.airPurgeInterval} 分钟/次`);
      console.log(`汤保温自动检测时间修改为: ${this.tempDetectionInterval} 分钟/次`);
    },
  },
};
</script>

<style scoped>
/* 可根据需要自定义样式 */
</style>
