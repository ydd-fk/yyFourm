import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/page/Index'
import Login from '@/page/Login'
import Register from '@/page/Register'
import PersonalCenter from '@/page/PersonalCenter'
import PostsDetail from "@/page/PostsDetail"
import OtherPersonalCenter from "@/page/OtherPersonalCenter";
import AlertPosts from "@/page/AlertPosts";
import AddPosts from "@/page/AddPosts";
import SearchPostsItems from "@/page/SearchPosts";
import authUtil from "@/utils/authUtil";
import store from "@/store";
import MyMessage from "@/page/MyMessage";

Vue.use(Router)



const router = new Router({
  routes: [
    {
      path: '/index',
      name: 'Index',
      component: Index
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/Register',
      name: 'Register',
      component: Register
    },
    {
      path: '/center',
      name: 'PersonalCenter',
      component: PersonalCenter,
    },
    {
      path:'/detail',
      name:'PostsDetail',
      component:PostsDetail
    },
    {
      path:'/other',
      name:'OtherPersonalCenter',
      component:OtherPersonalCenter
    },
    {
      path:'/alert',
      name:'AlertPosts',
      component:AlertPosts
    },
    {
      path:'/add',
      name:'AddPosts',
      component:AddPosts
    },
    {
      path:'/search',
      name:'SearchPostsItems',
      component:SearchPostsItems
    },
    {
      path:'/message',
      name:'Message',
      component:MyMessage
    },
    {
      path:'*',
      redirect:{name:'Index'}
    }
  ]
})


//vue在第一次被创建的时候会触发created生命周期函数，created函数中调用了vuex的setUser异步函数，
// 由于是异步，所以需要等待setUser执行完成，否则全局变量（vuex）user会影响其他以以user为参数的函数
router.beforeEach((to, from, next)=>{
  if (authUtil.getAuthorization() == null || authUtil.getAuthorization() == '') next();
  else {
    let interval = setInterval(()=>{
      if(store.state.user.id>0) {
        next();
        clearInterval(interval);
      }
    },100)
  }
})


export default router;
