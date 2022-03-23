<template>
<div id="headerBox">
  <span class="logo">
    <span @click="logoClick">YY论坛</span>
  </span>
  <div class="searchBox" @mouseleave="mouseleave" @mousemove="mouseout">
    <el-input @focus="inputFocus"
              class="searchInput"
              size="small"
              placeholder="搜索关键词"
              @keyup.enter.native="searchBtnClick"
              v-model="searchStr">
      <el-button slot="append" icon="el-icon-search" @click="searchBtnClick"></el-button>
    </el-input>
    <div class="searchBottomBox"
         v-if="historyIsShow">
      <div v-if="historyList.length!=0">
        <p class="title">
          <span class="txt">搜索历史</span>
          <span class="iconBox" @click="clearHistoryClick">
          清空<icon name="lajitong" :w="12" :h="12" color="black"></icon>
        </span>
        </p>
        <p class="historyItem" v-for="(item,index) in historyList">
          <span class="txt" @click="historyItemClick(item)">{{ item }}</span>
          <span class="delBtn" @click="delHistoryClick(index)">
          <icon name="chacha" :w="14" :h="14" color="black"></icon>
        </span>
        </p>
      </div>
      <div class="noneTips" v-if="historyList.length==0">
        <p>暂无搜索记录</p>
      </div>
    </div>
  </div>
  <div class="rightBtn" v-if="user.id==0">
    <el-button size="small" type="primary" plain @click="submitBtnClick">登录/注册</el-button>
  </div>

  <div class="rightBtn" v-else="user.id==0">
    <el-dropdown>
  <span class="el-dropdown-link">
    <header-image :size="50" :url="user.icon"/>
  </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <span @click="centerBtnClick">个人中心</span>
        </el-dropdown-item>
        <el-dropdown-item>
          <span @click="()=>this.$router.push('/message').catch()">消息</span>
        </el-dropdown-item>
        <el-dropdown-item>·
          <span @click="exitBtnClick">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
  <el-button v-if="user.id!=0&&user.id!=''"
             type="primary"
             style="width: 80px;margin-right: 10px" size="small"
             @click="$router.push('/add')">发帖</el-button>
</div>
</template>

<script>
import HeaderImage from "@/components/HeaderImage";
import authUtil from "@/utils/authUtil";
export default {
  name: "Header",
  components: {HeaderImage},
  created() {
    this.searchStr = this.searchKey;
    this.historyList = this.getHistory();
    console.log(this.historyList)
  },
  props:{
    searchKey:{
      type:String,
      default:''
    }
  },
  data(){
    return {
      searchStr:'',
      manyBtnShow:true,
      searchFlag:false,
      historyList:[],
      historyIsShow:false,
      isMouseLeave:false
    }
  },
  methods:{
    submitBtnClick(){
      this.$router.push('login')
    },
    logoClick(){
      this.$router.push('index').catch(e=>{})
    },
    centerBtnClick(){
      this.$router.push('center')
    },
    exitBtnClick(){
      this.$message.success("已退出登录")
      this.$store.commit('removeUser');
      this.$router.push("/index");
    },
    searchBtnClick(){
      this.historyList = this.addHistory(this.searchStr);
      if (this.$route.path!="/search") {
        this.$router.push({path: "/search",query:{str:this.searchStr}}).catch(e=>{})
      }else{
        this.$router.push({path: "/search",query:{str:this.searchStr}}).catch(e=>{})
        this.$emit("searchPosts",this.searchStr);
      }
    },
    delHistoryClick(index){
      this.historyList = this.historyList.filter((item,i)=>i!=index);
      this.setHistory(this.historyList);
    },
    clearHistoryClick(){
      this.historyList = [];
      this.clearHistory();
    },
    historyItemClick(str){
      this.searchStr=str;
      console.log(str)
      if (this.$route.path!="/search") {
        this.$router.push({path: "/search",query:{str:this.searchStr}}).catch(e=>{})
      }else{
        this.$router.push({path: "/search",query:{str:this.searchStr}}).catch(e=>{})
        this.$emit("searchPosts",this.searchStr);
      }
    },
    addHistory(searchStr){
      if (searchStr==null||searchStr=="") return;
      let list = this.getHistory();
      list = list.filter(item=>item!=searchStr);
      list.unshift(searchStr);
      if (list.length>=10) list=list.filter((item,index)=>index<=9);
      localStorage.setItem("history",JSON.stringify(list));
      return list;
    },
    getHistory(){
      let historyJson = localStorage.getItem("history");
      if (historyJson==null) return [];
      else return JSON.parse(historyJson);
    },
    clearHistory(){
      localStorage.setItem("history",JSON.stringify([]))
    },
    setHistory(historyList){
      localStorage.setItem("history",JSON.stringify(historyList))
    },
    inputFocus(){
      if (this.searchStr==""||this.searchStr==null) this.historyIsShow=true;
      else this.historyIsShow=false;
    },
    windowClick(){
      if (this.isMouseLeave) this.historyIsShow=false;
    },
    mouseleave(){
      this.isMouseLeave=true;
    },
    mouseout(){
      this.isMouseLeave=false;
    }
  },
  computed:{
    user(){
      return this.$store.state.user
    }
  },
  mounted() {
    window.addEventListener("click", this.windowClick)
  },
  beforeDestroy() {
    window.removeEventListener("click", this.windowClick)
  },
  watch:{
    searchStr(newStr){
      if (newStr!=null||newStr!="") this.historyIsShow=false;
      if (newStr==null||newStr=="") this.historyIsShow=true;
    }
  }
}
</script>

<style scoped lang="less">
@searchWidth:300px;
@historyBoxH:150px;
#headerBox {
  position: fixed;
  z-index: 100;
  top: 0;
  left: 0;
  right: 0;
  height: 58px;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  .logo{
    font-weight: bold;
    padding: 0 0 0 25px;
    flex: 1;
    span{
      cursor:pointer;
    }
  }
  .searchBox{
    position: absolute;
    top: 10px;
    .searchInput{
      width:@searchWidth;
    }
    .searchBottomBox{
      padding-top: 6px;
      background-color: #ffffff;
      position: relative;
      box-shadow: 0px 1px 4px #c4c4c4;
      width: @searchWidth;
      min-height: @historyBoxH;
      .historyItem{
        height: 30px;
        display: flex;
        align-items: center;
        color: #555666;
        font-size: 14px;
        padding-left: 8px;
        .txt{
          flex: 1;
        }
        .delBtn{
          display: none;
          margin-right: 8px;
        }
      }
      .historyItem:hover{
        background-color: #efefef;
      }
      .historyItem:hover .delBtn{
        display: inline-block;
      }
      .title{
        font-size: 10px;
        margin: 6px;
        display: flex;
        align-items: center;
        .txt{
          color: #cd0000;
          flex: 1;
        }
        .iconBox{
          margin-right: 5px;
          color: #727272;
          cursor: pointer;
        }
        .iconBox:hover{
          color: #2f2f2f;
        }
      }
      .noneTips{
        display: flex;
        justify-content: center;
        align-items: center;
        color: #727272;
        width: 100%;
        height: @historyBoxH;
        font-size: 14px;
        font-weight: bold;
      }
    }
  }
  .rightBtn{
    padding: 0 30px 0 0;
  }
}

</style>
