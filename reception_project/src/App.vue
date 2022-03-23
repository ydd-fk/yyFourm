<template>
  <div id="app">
    <transition name="app">
      <router-view/>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'App',
  created() {
    this.$store.dispatch("initUser")
  }
}
</script>

<style>
*{
  padding: 0;
  margin: 0;
}
.app-enter-active, .app-leave-avtive {
  transition: all 0.5s;
}
.app-enter, .app-leave-to {
  opacity: 0.5;
  transform :translate3d(0,20px,0);
}
</style>
