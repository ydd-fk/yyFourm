<template>
<div v-if="timeString!=null">
  <p class="time">{{timeString}}</p>
</div>
</template>

<script>
export default {
  name: "HistoryTime",
  props: ['preTime','time'],
  methods:{
    isToday(time){
      let nowTimeFormat = this.$moment(new Date()).format("YYYY-MM-DD");
      let timeFormat = this.$moment(time).format("YYYY-MM-DD")
      if (nowTimeFormat==timeFormat) return this.$moment(time).format("HH:mm:ss");
      else return this.$moment(time).format("YYYY-MM-DD HH:mm:ss");
    }
  },
  computed:{
    timeString(){
      let time = new Date(this.time);
      if (this.preTime==null) return this.isToday(time);
      let preTime = new Date(this.preTime);
      let minCha = (time.getTime() - preTime.getTime())/1000/60;
      if (parseInt(minCha)<=5) return null;
      else return this.isToday(time);
    }
  }
}
</script>

<style scoped>
.time{
  color: #727272;
  font-size: 12px;
  text-align: center;
  padding: 5px;
}
</style>
