<template>
  <div>
    <el-menu
        :default-active="activeIndex"
        class="custom-menu"
        @select="handleSelect"
        :background-color="menuBackgroundColor"
        :text-color="menuTextColor"
        :active-text-color="menuActiveTextColor"
    >
      <!-- 菜单组 1 -->
      <el-menu-item-group title="返回点餐">
        <el-menu-item index="1">
          点餐
        </el-menu-item>
      </el-menu-item-group>

      <!-- 菜单组 2 -->
      <el-menu-item-group title="设置页面">
        <el-menu-item index="2">
          销售统计
        </el-menu-item>
        <el-menu-item index="3">
          清洗操作
        </el-menu-item>
        <el-menu-item index="4">
          自动运行
        </el-menu-item>
        <el-menu-item index="5">
          手动操作
        </el-menu-item>
        <el-menu-item index="6">
          报警设置
        </el-menu-item>
        <el-menu-item index="7">
          份量设置
        </el-menu-item>
        <el-menu-item index="8">
          点餐设置
        </el-menu-item>
        <el-menu-item index="9">
          价格设置
        </el-menu-item>
        <el-menu-item index="10">
          机器账户设置
        </el-menu-item>
      </el-menu-item-group>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: 'Menu',
  data() {
    return {
      activeIndex: this.getActiveIndex(), // 根据路由初始化选中项
      menuBackgroundColor: '#eeeeee',
      menuTextColor: '#333333',
      menuActiveTextColor: '#ffffff',
    };
  },
  methods: {
    handleSelect(key) {
      this.activeIndex = key; // 更新选中项
      switch (key) {
        case '1':
          this.$router.push('/');
          break;
        case '2':
          this.$router.push('/salesStatistics');
          break;
        case '3':
          this.$router.push('/cleaningOperation');
          break;
        case '4':
          this.$router.push('/automaticOperation');
          break;
        case '5':
          this.$router.push('/manualOperation');
          break;
        case '6':
          this.$router.push('/alarmSettings');
          break;
        case '7':
          this.$router.push('/portionSettings');
          break;
        case '8':
          this.$router.push('/orderingSettings');
          break;
        case '9':
          this.$router.push('/priceSettings');
          break;
        case '10':
          this.$router.push('/setAccount');
          break;
        default:
          this.$router.push('/');
      }
    },
    getActiveIndex() {
      const path = this.$route.path;
      switch (path) {
        case '/salesStatistics':
          return '2';
        case '/cleaningOperation':
          return '3';
        case '/automaticOperation':
          return '4';
        case '/manualOperation':
          return '5';
        case '/alarmSettings':
          return '6';
        case '/portionSettings':
          return '7';
        case '/orderingSettings':
          return '8';
        case '/priceSettings':
          return '9';
        case '/setAccount':
          return '10';
        default:
          return '1'; // 默认项：点餐页面
      }
    }
  },
  watch: {
    '$route'(to) {
      this.activeIndex = this.getActiveIndex(); // 路由变化时更新选中项
    }
  }
};
</script>

<style scoped>
.custom-menu {
  width: 200px;
  border-right: none;
}

.custom-menu .el-menu-item {
  background-color: #eeeeee;
  color: #333333;
  transition: background-color 0.3s, color 0.3s;
  cursor: pointer; /* 确保整个菜单项看起来是可以点击的 */
}

.custom-menu .el-menu-item:hover {
  background-color: #e0e0e0;
}

.custom-menu .el-menu-item.is-active {
  background-color: #409EFF !important;
  color: #ffffff !important;
}
</style>
