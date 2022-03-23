<template>
<div id="headerBox">
  <span class="logo" @click="logoClick">
    YY论坛
  </span>
  <div class="searchBox">

  </div>
</div>
</template>

<script>
export default {
  name: "Header",
  data(){
    return {
      searchStr:'',
      manyBtnShow:true,
      searchFlag:false
    }
  },
  methods:{
    submitBtnClick(){
      this.$router.push('login')
    },
    logoClick(){
      this.$router.push('index')
    },
    centerBtnClick(){
      this.$router.push('center')
    },
    exitBtnClick(){
      this.$router.push('login')
    },
    searchFocus(){
      //this.$refs.historyBox.style.opacity=1
    }
  }
}
</script>

<style scoped lang="less">
#headerBox {
  height: 58px;
  width: 100%;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  .logo{
    font-weight: bold;
    padding: 0 0 0 25px;
    flex: 1;
  }
  .logo:hover{
    cursor:pointer;
  }
  .rightBtn{
    padding: 0 30px 0 0;
  }
}
</style>
