<template>
  <div style="text-align: center; padding-top: 50px; margin-top: 100px;">
    <!-- 手动清洗按钮 -->
    <el-button type="primary" @click="manualClean">手动清洗</el-button>

    <div style="margin: 200px 0;"></div>

    <!-- 机器自动清洗显示 -->
    <div>
      <span>机器自动清洗</span>
      <el-input-number v-model="cleanInterval" :min="1" :max="1440" :step="1" size="small" controls-position="right" style="width: 100px; margin: 0 10px;" />
      <span>分钟/次</span>
    </div>

    <div style="margin: 30px 0;"></div>

    <!-- 修改自动清洗时间按钮 -->
    <el-button type="primary" @click="updateCleanTime">修改自动清洗时间</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      cleanInterval: 100, // 自动清洗间隔时间
    };
  },
  methods: {
    manualClean() {
      // 手动清洗操作
      console.log("手动清洗执行中...");
    },
    updateCleanTime() {
      // 修改自动清洗时间的操作
      console.log(`自动清洗时间修改为: ${this.cleanInterval} 分钟/次`);
    },
  },
};
</script>

<style scoped>
.el-button{
  width: 180px;
}
</style>
