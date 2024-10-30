<template>
  <div style="text-align: center; padding-top: 50px;">
    <el-table
        :data="settings"
        style="width: 400px; margin: auto; "
        border
    >
      <el-table-column label="设置项" width="200">
        <template #default="scope">
          <span>{{ scope.row.label }}</span>
        </template>
      </el-table-column>

      <el-table-column label="参数值" width="200">
        <template #default="scope">
          <el-input-number
              v-model="scope.row.value"
              :min="scope.row.min"
              :max="scope.row.max"
              :step="1"
              size="small"
              controls-position="right"
              style="width: 100px;"
          />
          <span>{{ scope.row.unit }}</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改按钮 -->
    <el-button type="primary" @click="updateSettings" style="margin-top: 20px;">设置</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      settings: [
        { label: "汤自动循环排空气", value: 20, min: 1, max: 1440, unit: "分钟/次" },
        { label: "汤保温自动检测", value: 10, min: 1, max: 1440, unit: "分钟/次" },
        { label: "保温最高温度", value: 100, min: 0, max: 150, unit: "°C" },
        { label: "保温最低温度", value: 90, min: 0, max: 150, unit: "°C" }
      ]
    };
  },
  methods: {
    updateSettings() {
      this.settings.forEach(setting => {
        console.log(`${setting.label} 修改为: ${setting.value} ${setting.unit}`);
      });
    }
  }
};
</script>

<style scoped>
/* Set a solid background for the entire table */
.el-table {
  background-color: #ffffff;
}

/* Optional: Add a border radius for styling */
.el-table {
  border-radius: 8px;
}

/* Optional: Add padding to the cells */
.el-table .el-table__cell {
  padding: 10px;
}
</style>
