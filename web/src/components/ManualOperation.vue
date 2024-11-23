<!-- components/ManualOperation.vue -->
<template>
  <div class="container">
    <!-- Robot and Machine Operations -->
    <div class="button-group">
      <h3 class="section-title">机器人和取餐</h3>
      <div class="button-columns">
        <div v-for="(btn, index) in buttonsGroup1" :key="`btn1-${index}`" class="button-column">
          <el-button type="primary" :style="buttonStyle" @click="openDialog(btn.id, btn.name)">
            {{ btn.name }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- Turntable and Bowl Operations -->
    <div class="button-group">
      <h3 class="section-title">碗粉丝</h3>
      <div class="button-columns">
        <div v-for="(btn, index) in buttonsGroup2" :key="`btn2-${index}`" class="button-column">
          <el-button type="primary" :style="buttonStyle" @click="openDialog(btn.id, btn.name)">
            {{ btn.name }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- Steam and Temperature Control -->
    <div class="button-group">
      <h3 class="section-title">蒸汽和温度</h3>
      <div class="button-columns">
        <div v-for="(btn, index) in buttonsGroup3" :key="`btn3-${index}`" class="button-column">
          <el-button type="primary" :style="buttonStyle" @click="openDialog(btn.id, btn.name)">
            {{ btn.name }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- Fan and Vibration Testing -->
    <div class="button-group">
      <h3 class="section-title">风扇和振动</h3>
      <div class="button-columns">
        <div v-for="(btn, index) in buttonsGroup4" :key="`btn4-${index}`" class="button-column">
          <el-button type="primary" :style="buttonStyle" @click="openDialog(btn.id, btn.name)">
            {{ btn.name }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- Testing and Weighing Operations -->
    <div class="button-group">
      <h3 class="section-title">称重</h3>
      <div class="button-columns">
        <div v-for="(btn, index) in buttonsGroup5" :key="`btn5-${index}`" class="button-column">
          <el-button type="primary" :style="buttonStyle" @click="openDialog(btn.id, btn.name)">
            {{ btn.name }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- Ingredient Dispensing -->
    <div class="button-group">
      <h3 class="section-title">配料分发</h3>
      <div class="button-columns">
        <div v-for="(btn, index) in buttonsGroup6" :key="`btn6-${index}`" class="button-column">
          <el-button type="primary" :style="buttonStyle" @click="openDialog(btn.id, btn.name)">
            {{ btn.name }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- Fixed Buttons -->
    <div class="fixed-buttons">
      <el-button type="success" class="reset-button" @click="resetSystem">复位</el-button>
      <el-button type="danger" class="emergency-button" @click="emergencyStop">急停</el-button>
    </div>

    <!-- Dialog for Input Parameters -->
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
  name: 'ManualOperation',
  data() {
    return {
      buttonsGroup1: [
        { id: 1, name: "机器人重置" },
        { id: 2, name: "机器人取粉丝" },
        { id: 3, name: "机器人取菜蓝" },
        { id: 4, name: "机器人取碗" },
        { id: 5, name: "机器人放碗" },
        { id: 6, name: "机器人出餐" },
        { id: 7, name: "取餐口复位" },
        { id: 8, name: "取餐口出餐" }
      ],

      buttonsGroup2: [
        { id: 9, name: "出碗" },
        { id: 10, name: "粉丝仓复位" },
        { id: 11, name: "移动粉丝仓（编号）" },
        { id: 12, name: "粉丝仓出粉丝" },
        { id: 13, name: "装菜勺复位" },
        { id: 14, name: "装菜勺装菜" },
        { id: 15, name: "装菜勺倒菜" }
      ],

      buttonsGroup3: [
        { id: 16, name: "蒸汽打开" },
        { id: 17, name: "蒸汽关闭" },
        { id: 18, name: "加蒸汽盖下降" },
        { id: 19, name: "加汤盖下降" },
        { id: 20, name: "加汤蒸汤盖上升" },
        { id: 21, name: "加汤（秒）" },
        { id: 22, name: "汤管排气（秒）" },
        { id: 23, name: "汤加热至（度）" },
        { id: 24, name: "加蒸汽（秒）" },
        { id: 25, name: "加蒸汽和汤" }
      ],

      buttonsGroup4: [
        { id: 26, name: "后箱风扇开" },
        { id: 27, name: "后箱风扇关" },

      ],

      buttonsGroup5: [
        { id: 33, name: "一号配菜（g）" },
        { id: 34, name: "二号配菜（g）" },
        { id: 37, name: "1称重清0" },
        { id: 38, name: "1标重500g（编号）" },
        { id: 37, name: "2称重清0" },
        { id: 38, name: "2标重500g（编号）" }
      ],

      buttonsGroup6: [
        { id: 28, name: "切肉机切肉（份量）" },
        { id: 29, name: "震动器（秒）" },
        { id: 32, name: "出料开关（秒）" },
        { id: 30, name: "震动料开关打开" },
        { id: 31, name: "震动料开关关闭" },
        { id: 39, name: "打开称重盒1" },
        { id: 40, name: "关闭称重盒1" },
        { id: 41, name: "打开称重盒2" },
        { id: 42, name: "关闭称重盒2" }
      ],
      dialogVisible: false,
      parameter: '',
      currentButtonId: null,
      currentButtonName: '',

      errorHandler: null
    };
  },
  computed: {
    buttonStyle() {
      return {
        width: '200px',  // 所有按钮宽度设置为200px
      };
    }
  },
  methods: {
    async sendRequest(url) {
      try {
        const response = await axios.get(url);
        if (response.data.code === 200) {
          this.$message.success(`操作成功：${response.data.data}`);
        } else {
          this.$message.error(`操作失败：${response.data.message}`)
        }
        // this.$message.success(`操作成功：${response.data}`);
        console.log(response.data);
      } catch (error) {
        this.$message.error('操作失败');
        console.error(error);
      }
    },
    openDialog(id, name) {
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
    }
  }
};
</script>

<style scoped>
.container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr 1fr;
  grid-gap: 20px;
  padding: 20px;
  height: 100vh;
  box-sizing: border-box;
}

.button-group {
  height: 650px;
  width: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border: 1px solid var(--gray);
  padding: 10px;
  box-sizing: border-box;
  border-radius: 12px; /* 圆角设置 */
  overflow: hidden; /* 防止子元素溢出父容器的圆角 */
}

.button-columns {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.section-title {
  color: var(--deep-blue);
  background-color: var(--gray);
  padding: 5px 10px;
  border-radius: 5px;
  text-align: center;
  margin-bottom: 10px;
}

.el-button {
  margin: 5px 0;
  font-size: 15px;
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
