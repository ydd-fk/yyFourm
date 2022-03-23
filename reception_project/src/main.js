import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import IconSvg from 'vue2-svg-icon/Icon';
import store  from '@/store';
import 'default-passive-events';
import moment from 'moment';

Vue.use(ElementUI);
Vue.component('icon',IconSvg);
Vue.config.productionTip = false
Vue.prototype.$formatDate = formatDate;
Vue.prototype.$moment = moment;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})


function  formatDate(dataObj){
  let y = dataObj.getFullYear();  //年
  let m = dataObj.getMonth() + 1;  //月
  if(m < 10){ m = '0' + m }
  let d = dataObj.getDate();  //日
  if(d < 10){ d = '0' + d }
  let h = dataObj.getHours();  //时
  if(h < 10){ h = '0' + h }
  let mm = dataObj.getMinutes();  //分
  if(mm < 10){ mm = '0' + mm }
  let s = dataObj.getSeconds();  //秒
  if(s < 10){ s = '0' + s }
  let timeStr = y+"-"+m+"-"+d+" "+h+":"+mm+":"+s;
  return timeStr;
}
