<template>
<div class="alertOutBox">
  <Header></Header>
  <div class="alertBox">
    <div class="alertBoxTitle">
      <span>新增帖子</span>
    </div>
    <p>
      帖子标题
    </p>
    <el-input v-model="title" class="postsInput" placeholder="请输入标题"/>
    <p>
      帖子描述
    </p>
    <el-input v-model="detail" class="postsInput" placeholder="请输入描述的内容" type="textarea" rows="6"></el-input>
    <p>
      上传图片
    </p>
    <div class="iconBox">
      <div class="iconUpload">
        <i class="el-icon-plus avatar-uploader-icon"></i>
        <input multiple="multiple" type="file" @change="fileChange"/>
      </div>
<!--      图片预览条目-->

      <div class="iconItem" v-for="(item,index) in icons" :key="item.index">
        <img :src="item.url">
        <div class="svgBox" @click="delBtn(index)">
          <icon name="chacha" w="20" h="20" color="black"></icon>
        </div>

      </div>
    </div>

    <div class="btnBox">
      <el-button size="small" type="success" @click="addClick">发布</el-button>
      <el-button size="small" @click="$router.go(-1);">返回</el-button>
    </div>
  </div>

</div>
</template>

<script>
import Header from "@/components/common/Header";
import postsApi from "@/api/posts";
import {SUCCESS_CODE} from "@/utils/codeUtil";
export default {
  name: "AddPosts",
  components: {Header},
  data(){
    return{
      imageUrl:'',
      index:1,
      icons:[],
      detail:'',
      title:''
    }
  },
  methods:{
    fileChange(e){
      for (let i=0;i<e.target.files.length;i++){
        let file = e.target.files[i]
        let URL = window.URL || window.webkitURL;
        let imgURL = URL.createObjectURL(file);
        let icon = {
          url:imgURL,
          index:this.index,
          file
        }
        this.index++;
        this.icons.push(icon)
      }
    },
    delBtn(index){
      this.icons = this.icons.filter((item,i)=>i!=index)
    },
    async addClick(){
      let fileArr = [];
      this.icons.forEach((item)=>{fileArr.push(item.file)})
      let formData = new FormData();
      if (fileArr.length!=0){
        fileArr.forEach((file)=>{
          formData.append("icons",file);
        })
      }
      formData.append("title",this.title);
      formData.append("detail",this.detail);

      console.log("==fileArr==")
      console.log(fileArr)
      console.log("title=="+this.title)
      console.log("detail=="+this.detail)
      const {code} = await postsApi.addPosts({formData});
      if (code==SUCCESS_CODE){
        this.$message.success("发布成功")
      }else {
        this.$message.error("发布失败，稍后请重试")
      }
    }
  }
}
</script>

<style scoped lang="less">
@import "../style/alert";
</style>
