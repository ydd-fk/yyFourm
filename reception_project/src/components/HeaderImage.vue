<template>
  <div class="imgBox" @click="imgClick">
    <icon v-if="url==''||url==null" name="person" :w="size+5" :h="size+5" color="black"></icon>
    <el-avatar v-else="url==''||url==null" :size="size" :src="baseURL+'/img/'+url">
    </el-avatar>
  </div>

</template>

<script>
import config from "@/http/config";
export default {
name: "HeaderImage",
  props:{
    url:String,
    size:Number,
    id:{
      type:Number,
      default:null
    },
    isinit:{
      type:Boolean,
      default: false
    }
  },
  methods:{
    async imgClick(){
      if (this.id!=null) {
        try{
          await this.$router.push({path:"/other",query:{id:this.id}});
        }catch (e) {}
      }
      if (this.isinit==true) this.$emit("fatherInit");
    }
  },
  computed:{
    baseURL(){
      return config.baseURL;
    }
  }
}
</script>

<style scoped>
div.imgBox{
  display: inline-block;
  cursor:pointer;
}
</style>
