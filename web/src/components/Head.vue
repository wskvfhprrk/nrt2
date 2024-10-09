<!-- components/Menu.vue -->
<template>
  <div class="title">
    <el-avatar :size="50" src="https://empty" @error="errorHandler">
      <img src='../assets/img/logo.png'/>
    </el-avatar>
    <span class="text">牛羊肉汤自动售卖机后台管理</span>
  </div>
</template>

<script>
export default {
  name: 'Head'
};
</script>

<style scoped>
/* 可根据需要添加菜单样式 */
.title{
  display: flex;             /* 使用 flexbox 布局 */
  align-items: center;        /* 垂直居中 */
  font-size: 25px;
  color: #67c23a;
  background-color: #ffffff;
  height: 60px;/*高度，没有高度字不能距中*/
  /*justify-content: center;    !* 水平居中 *!*/
}
.el-avatar{
  margin-left: 30px;
  margin-right: 10px;
}
</style>
