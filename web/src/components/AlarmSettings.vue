<template>
  <div style="padding: 20px; max-width: 600px; margin: auto;">

    <!-- 添加项目 -->
    <div v-for="(item, index) in addItems" :key="index" style="margin-bottom: 10px; display: flex; align-items: center;">
      <span style="width: 150px;font-size: 15px;">{{ item.label }}</span>
      <el-input-number v-model="item.value" :min="0" :step="1" size="small" controls-position="right" style="width: 100px; margin: 0 10px;" />
      <span style="font-size: 15px;">{{ item.unit }}</span>
    </div>

    <!-- 清零和添加按钮 -->
    <div style="margin: 30px 0;">
      <el-button type="danger" @click="resetValues" style="margin-right: 10px;">所有清零</el-button>
      <el-button type="success" @click="confirmAdd">确认添加</el-button>
    </div>

    <!-- 销售项目设置 -->
    <div v-for="(item, index) in saleItems" :key="index" style="margin-bottom: 20px; display: flex; align-items: center;">
      <span style="width: 150px;font-size: 15px;">{{ item.label }}</span>
      <el-input-number v-model="item.saleAmount" :min="0" :step="0.1" size="small" controls-position="right" style="width: 120px; " />
      <span style="font-size: 15px;">量报警警</span>
      <el-input-number v-model="item.alarmThreshold" :min="0" :step="0.01" size="small" controls-position="right" style="width: 100px; " />
      <span style="font-size: 15px;">量停止售</span>
    </div>

    <!-- 修改参数按钮 -->
    <el-button type="primary" @click="updateParameters" style="margin-top: 20px;margin-left: 200px;">修改参数</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 添加项目的数据
      addItems: [
        { label: '添加牛（羊）肉', value: 10000, unit: 'g' },
        { label: '添加牛（羊）杂', value: 10000, unit: 'g' },
        { label: '添加香菜', value: 1000, unit: 'g' },
        { label: '添加葱花', value: 1000, unit: 'g' },
        { label: '添加粉丝', value: 100, unit: '份' },
        { label: '添加饼丝', value: 0, unit: '份' }
      ],
      // 销售项目的数据
      saleItems: [
        { label: '牛（羊）肉', saleAmount: 0.5, alarmThreshold: 0.95 },
        { label: '牛（羊）杂', saleAmount: 0.5, alarmThreshold: 0.95 },
        { label: '香菜', saleAmount: 0.5, alarmThreshold: 0.95 },
        { label: '葱花', saleAmount: 0.5, alarmThreshold: 0.95 },
        { label: '粉丝', saleAmount: 0.5, alarmThreshold: 0.95 },
        { label: '饼丝', saleAmount: 0.5, alarmThreshold: 0.95 }
      ]
    };
  },
  methods: {
    resetValues() {
      // 将添加项目数据清零
      this.addItems.forEach(item => {
        item.value = 0;
      });
      console.log("所有数据已清零");
    },
    confirmAdd() {
      // 确认添加的操作
      console.log("添加数据确认：", this.addItems);
    },
    updateParameters() {
      // 更新参数的操作
      console.log("修改后的销售项目参数：", this.saleItems);
    }
  }
};
</script>

<style scoped>
div.el-input{
  width: 100px;
}
.el-input__wrapper {
  padding-left: 0;
  padding-right: 42px;
}
</style>
