<template>
<div class="alertOutBox">
  <Header></Header>
  <div class="alertBox">
    <div class="alertBoxTitle">
      <span>修改帖子</span>
    </div>
    <p>
      帖子标题
    </p>
    <el-input v-model="posts.title" class="postsInput" placeholder="请输入标题"/>
    <p>
      帖子描述
    </p>
    <el-input v-model="posts.detail" class="postsInput" placeholder="请输入描述的内容" type="textarea" rows="6"></el-input>
    <p>
      上传图片
    </p>
    <div class="iconBox">
      <div class="iconUpload">
        <i class="el-icon-plus avatar-uploader-icon"></i>
        <input  @change="fileChange"  multiple="multiple" type="file"/>
      </div>
<!--      图片预览条目-->
      <div class="iconItem" v-for="(item,index) in posts.icons">
        <img :src="baseURL+'/img/'+item.url">
        <div class="svgBox" @click="oldIconDelClick(index)">
          <icon name="chacha" w="20" h="20" color="black"></icon>
        </div>

      </div>
      <div class="iconItem" v-for="(item,index) in newIcons">
        <img :src="item.url">
        <div class="svgBox" @click="newIconDelClick(index)">
          <icon name="chacha" w="20" h="20" color="black"></icon>
        </div>

      </div>
    </div>

    <div class="btnBox">
      <el-button size="small" type="primary" @click="modifyClick">保存</el-button>
      <el-button size="small">取消</el-button>
    </div>
  </div>

</div>
</template>

<script>
import Header from "@/components/common/Header";
import config from "@/http/config";
import postsApi from "@/api/posts";
import {SUCCESS_CODE} from "@/utils/codeUtil";
export default {
  name: "AlertPosts",
  components: {Header},
  created(){
    const {posts} = this.$route.params;
    if (this.$route.params.posts!=null) this.posts=posts;
  },
  data(){
    return{
      imageUrl:'',
      posts:{},
      newIcons:[],
      delIconName:[]
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
          file
        }
        this.newIcons.push(icon)
      }
    },
    oldIconDelClick(index){
      this.delIconName.push(this.posts.icons[index].url);
      this.posts.icons = this.posts.icons.filter((item,i)=>i!=index);
    },
    newIconDelClick(index){
      this.newIcons = this.newIcons.filter((item,i)=>i!=index);
    },
    async modifyClick(){
      let formData = new FormData();
      const{detail,title,id}=this.posts;
      formData.append("detail",detail);
      formData.append("postsID",id);
      formData.append("title",title);
      this.delIconName.forEach(item=>{
        formData.append("delIcons",item);
      })
      this.newIcons.forEach(item=>{
        formData.append("newIcons",item.file)
      })

      const {code} = await postsApi.modifyPosts({formData})
      if (code==SUCCESS_CODE) {
        this.$message.success("保存成功")
        this.$router.replace("/center")
      }else this.$message.error("修改失败")
    }
  },
  computed:{
    baseURL(){
      return config.baseURL;
    }
  }
}
</script>

<style scoped lang="less">
@import "../style/alert";
</style>
