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
    <el-dialog title="输入参数" v-model="dialogVisible" :before-close="handleClose">
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
        { id: 1, name: "机器人重置" },
        { id: 2, name: "机器人取碗" },
        { id: 3, name: "机器人出汤" },
        { id: 4, name: "取餐口复位" },
        { id: 5, name: "取餐口出餐" },
        { id: 6, name: "调料机测试（配方）" },       // 需要参数
        { id: 7, name: "转台复位" },
        { id: 8, name: "转台工位（数字）" },         // 需要参数
        { id: 9, name: "碗复位" },
        { id: 10, name: "碗向上" },
        { id: 11, name: "碗向下" },
        { id: 12, name: "关闭汤蒸汽阀" },
        { id: 13, name: "抽汤（秒）" },           // 需要参数
        { id: 14, name: "抽汤排气（秒）" },       // 需要参数
        { id: 15, name: "后箱风扇开" },
        { id: 16, name: "后箱风扇关" },
        { id: 17, name: "震动器测试（秒）" },       // 需要参数
        { id: 18, name: "蒸汽打开" },
        { id: 19, name: "蒸汽关闭" },
        { id: 20, name: "碗加蒸汽（秒）" },         // 需要参数
        { id: 21, name: "汤加热温度（度）" },      // 需要参数
        { id: 22, name: "弹簧货道（编号）" },       // 需要参数
        { id: 23, name: "配菜称重盒打开（编号）" }, // 需要参数
        { id: 24, name: "配菜称重盒关闭（编号）" }, // 需要参数
        { id: 25, name: "一号配菜电机（g）" },      // 需要参数
        { id: 26, name: "二号配菜电机（g）" },      // 需要参数
        { id: 27, name: "三号配菜电机（g）" }       // 需要参数
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
      // 检查名称中是否包含括号，以确定是否需要参数。
      if (name.includes('（') && name.includes('）')) {
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
  display: flex; /* 设置为flex布局 */
  flex-direction: column; /* 设置容器内元素垂直方向排列 */
  justify-content: center; /* 垂直方向居中容器内元素 */
  align-items: center; /* 水平方向居中容器内元素 */
  height: calc(100vh - 100px); /* 保持原来的高度 */
  width: 100%; /* 容器占满全宽 */
  padding: 0 20px; /* 设置容器左右内边距 */
  box-sizing: border-box; /* 包括内边距和边框在内的大小 */
  background-color: transparent; /* 透明背景 */
}


.button-columns {
  display: flex; /* 使用flex布局 */
  justify-content: space-between;
  width: 100%; /* 宽度设为100% */
  max-width: 1200px; /* 设置最大宽度 */
  margin: 0 auto; /* 自动左右外边距，用于居中 */
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
