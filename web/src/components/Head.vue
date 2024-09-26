<!-- components/Menu.vue -->
<template>
  <div class="title">
    牛羊肉汤自动售卖机后台管理
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
  font-size: 20px;
  color: #67c23a;
  background-color: #ffffff;
  height: 60px;/*高度，没有高度字不能距中*/
  /*justify-content: center;    !* 水平居中 *!*/
}
</style>
