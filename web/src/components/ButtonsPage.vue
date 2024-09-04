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
      <h3 class="section-title">转台和碗</h3>
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
  name: 'ButtonsPage',
  data() {
    return {
      buttonsGroup1: [
        {id: 1, name: "机器人重置"},
        {id: 2, name: "机器人取碗"},
        {id: 3, name: "机器人出汤"},
        {id: 4, name: "取餐口复位"},
        {id: 5, name: "取餐口出餐"}
      ],
      buttonsGroup2: [
        {id: 6, name: "转台复位"},
        {id: 7, name: "工位（数）"},
        {id: 8, name: "碗复位"},
        {id: 9, name: "碗向上"},
        {id: 10, name: "碗向下"}
      ],
      buttonsGroup3: [
        {id: 11, name: "蒸汽打开"},
        {id: 12, name: "蒸汽关闭"},
        {id: 13, name: "关汤蒸汽阀"},
        {id: 14, name: "抽汤（秒）"},
        {id: 15, name: "汤管排气（秒）"},
        {id: 16, name: "汤加热至（度）"},
        {id: 17, name: "碗加蒸汽（秒）"}
      ],
      buttonsGroup4: [
        {id: 18, name: "后箱风扇开"},
        {id: 19, name: "后箱风扇关"},
        {id: 20, name: "震动器（秒）"}
      ],
      buttonsGroup5: [
        {id: 21, name: "一号配菜（g）"},
        {id: 22, name: "二号配菜（g）"},
        {id: 23, name: "三号配菜（g）"}
      ],
      buttonsGroup6: [
        {id: 24, name: "调料机（配方）"},
        {id: 25, name: "弹簧货道（编号）"},
        {id: 26, name: "称重盒开（编号）"},
        {id: 27, name: "称重盒关（编号）"}
      ],
      dialogVisible: false,
      parameter: '',
      currentButtonId: null,
      currentButtonName: ''
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
        this.$message.success(`操作成功：${response.data}`);
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
  display: flex;
  flex-direction: column;
  align-items: center;
  border: 1px solid #ddd;
  padding: 10px;
  box-sizing: border-box;
  border-radius: 12px; /* 圆角设置 */
  overflow: hidden;    /* 防止子元素溢出父容器的圆角 */
}

.button-columns {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.section-title {
  color: red;
  background-color: white;
  padding: 5px 10px;
  border-radius: 5px;
  text-align: center;
  margin-bottom: 10px;
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
