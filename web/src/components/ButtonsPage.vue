<template>
  <div>
    <div class="container">
      <div class="button-columns">
        <div v-for="(column, index) in buttonColumns" :key="index" class="button-column">
          <el-button v-for="btn in column" :key="btn.id" type="primary" @click="openDialog(btn.id, btn.name)">
            {{ btn.name }}
          </el-button>
        </div>
      </div>
    </div>
    <div class="fixed-buttons">
      <el-button type="success" class="reset-button" @click="resetSystem">复位</el-button>
      <el-button type="danger" class="emergency-button" @click="emergencyStop">急停</el-button>
    </div>
    <el-dialog title="输入参数" :visible="dialogVisible" :before-close="handleClose">
      <el-input v-model="parameter" placeholder="请输入参数"></el-input>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitParameter">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';

const baseURL = 'http://127.0.0.1:8080/buttonAction';

export default {
  name: 'ButtonsPage',
  data() {
    return {
      buttons: [
        {id: 1, name: "机器人重置"},
        {id: 2, name: "机器人取碗"},
        {id: 3, name: "机器人出汤"},
        {id: 4, name: "取餐口复位"},
        {id: 5, name: "取餐口出餐"},
        {id: 6, name: "调料机测试（配方）"},
        {id: 7, name: "转台复位"},
        {id: 8, name: "转台工位（数字）"},
        {id: 9, name: "碗复位"},
        {id: 10, name: "碗向上"},
        {id: 11, name: "碗向下"},
        {id: 12, name: "抽汤泵（秒）"},
        {id: 13, name: "汤开关开"},
        {id: 14, name: "汤开关关"},
        {id: 15, name: "后箱风扇开"},
        {id: 16, name: "后箱风扇关"},
        {id: 17, name: "震动器测试（秒）"},
        {id: 18, name: "蒸汽打开"},
        {id: 19, name: "蒸汽关闭"},
        {id: 20, name: "碗蒸汽杆向上"},
        {id: 21, name: "碗蒸汽杆向下"},
        {id: 22, name: "汤加热至（度）"},
        {id: 23, name: "弹簧货道（编号）"},
        {id: 24, name: "配菜称重盒打开（编号）"},
        {id: 25, name: "配菜称重盒关闭（编号）"},
        {id: 26, name: "1号配菜电机（g）"},
        {id: 27, name: "2号配菜电机（g）"},
        {id: 28, name: "3号配菜电机（g）"}
      ],
      buttonsPerColumn: 14,
      buttonColumns: [],
      dialogVisible: false,
      parameter: '',
      currentButtonId: null,
      currentButtonName: ''
    };
  },
  created() {
    this.buttonColumns = this.chunkArray(this.buttons, this.buttonsPerColumn);
  },
  methods: {
    async sendRequest(url) {
      try {
        const response = await axios.get(url);
        this.$message.success(`操作成功：${response.data}`);
        console.log(response.data);
      } catch (error) {
        this.$message.error('操作失败');
        console.error(error);
      }
    },
    openDialog(id, name) {
      // 针对需要输入参数的按钮弹出对话框
      if ([6, 8, 12, 17, 20, 21, 22, 23, 24, 25, 26, 27, 28].includes(id)) {
        this.dialogVisible = true;
      } else {
        this.dialogVisible = false;
        this.sendRequest(`${baseURL}/${id}`);
      }
      this.currentButtonId = id;
      this.currentButtonName = name;
    },
    handleClose() {
      this.dialogVisible = false;
    },
    submitParameter() {
      const url = `${baseURL}/${this.currentButtonId}?number=${this.parameter}`;
      this.sendRequest(url);
      this.dialogVisible = false;
      this.parameter = '';
    },
    emergencyStop() {
      this.sendRequest(`${baseURL}/emergencyStop`);
    },
    resetSystem() {
      this.sendRequest(`${baseURL}/reset`);
    },
    chunkArray(arr, size) {
      return Array.from({length: Math.ceil(arr.length / size)}, (_, index) =>
          arr.slice(index * size, index * size + size)
      );
    }
  }
};
</script>

<style scoped>
.title {
  text-align: center;
  margin-top: 20px;
  font-size: 24px;
}

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 100px);
  padding: 0 20px;
  box-sizing: border-box;
}

.button-columns {
  display: flex;
  justify-content: center;
  width: 100%;
  max-width: 1200px;
}

.button-column {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 10px;
}

.el-button {
  margin: 5px 0;
}

.fixed-buttons {
  position: fixed;
  bottom: 20px;
  right: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.reset-button {
  background-color: green;
  color: white;
  border: none;
  margin-bottom: 10px;
}

.emergency-button {
  background-color: red;
  color: white;
  border: none;
}
</style>
