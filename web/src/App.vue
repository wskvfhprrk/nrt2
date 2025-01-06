<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>

<style scoped>
#app {
  background-size: cover;
  background-position: center;
  min-height: 100vh;
  justify-content: center;
  align-items: center;
  background-color: var(--beige);
  overflow: auto; /* 确保内容可以滚动 */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
}

#app::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}

</style>

