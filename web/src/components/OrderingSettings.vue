<template>
  <div class="container">
    <el-form label-width="200px">
      <!-- Meat Category Selection -->
      <el-form-item label="选择肉类" >
        <el-radio-group v-model="selectedCategory">
          <el-radio-button label="牛肉和牛杂">牛肉和牛杂</el-radio-button>
          <el-radio-button label="羊肉和羊杂">羊肉和羊杂</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <!-- Options for Displaying Attributes -->
      <el-form-item label="显示辣椒">
        <el-radio-group v-model="showChili">
          <el-radio-button label="显示">显示</el-radio-button>
          <el-radio-button label="不显示">不显示</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="显示味精">
        <el-radio-group v-model="showMsg">
          <el-radio-button label="显示">显示</el-radio-button>
          <el-radio-button label="不显示">不显示</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="显示香菜">
        <el-radio-group v-model="showCilantro">
          <el-radio-button label="显示">显示</el-radio-button>
          <el-radio-button label="不显示">不显示</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="显示葱花">
        <el-radio-group v-model="showGreenOnion">
          <el-radio-button label="显示">显示</el-radio-button>
          <el-radio-button label="不显示">不显示</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <!-- Submit Button -->
      <el-button type="primary" style="margin: 40px 40px;" @click="modifyMenuPage">修改点餐页面</el-button>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selectedCategory: '牛肉和牛杂', // Either "牛肉, 牛杂" or "羊肉, 羊杂"
      showChili: '不显示',
      showMsg: '不显示',
      showCilantro: '不显示',
      showGreenOnion: '不显示'
    };
  },
  methods: {
    modifyMenuPage() {
      // Logic for modifying the menu page can be added here
      console.log('Menu settings updated');
    }
  }
};
</script>

<style scoped>
.container {
  width: 600px;
  margin: 100px auto; /* 使整个容器居中 */
}
.el-form-item {
  margin-bottom: 20px;
}
.button-container {
  display: flex;
  justify-content: center; /* 使按钮水平居中 */
  margin-top: 40px; /* 调整按钮上方的间距 */
}
::v-deep .el-form-item__label {
  font-size: 30px;
}
</style>
