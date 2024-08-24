(function(){"use strict";var e={8804:function(e,t,r){var n=r(6848),o=function(){var e=this,t=e._self._c;return t("div",{attrs:{id:"app"}},[t("router-view")],1)},a=[],i={name:"App"},s=i,l=r(1656),c=(0,l.A)(s,o,a,!1,null,null,null),u=c.exports,d=r(6178),m=function(){var e=this,t=e._self._c;return t("div",{staticClass:"outer-container",attrs:{id:"app"}},[t("div",{staticClass:"container"},[t("el-container",[t("el-main",[t("el-form",{attrs:{model:e.form,"label-width":"120px"}},[t("el-form-item",{attrs:{label:"选择食谱"}},[t("el-radio-group",{model:{value:e.form.selectedRecipe,callback:function(t){e.$set(e.form,"selectedRecipe",t)},expression:"form.selectedRecipe"}},[t("el-radio",{attrs:{label:"牛肉汤"}},[e._v("牛肉汤")]),t("el-radio",{attrs:{label:"牛杂汤"}},[e._v("牛杂汤")])],1)],1),t("el-form-item",{attrs:{label:"选择类别"}},[t("el-radio-group",{model:{value:e.form.selectedPrice,callback:function(t){e.$set(e.form,"selectedPrice",t)},expression:"form.selectedPrice"}},e._l(e.prices,(function(r){return t("el-radio",{key:r,attrs:{label:r}},[e._v(e._s(r)+"元")])})),1)],1),t("el-form-item",{attrs:{label:"选择口味"}},[t("el-radio-group",{model:{value:e.form.selectedSpice,callback:function(t){e.$set(e.form,"selectedSpice",t)},expression:"form.selectedSpice"}},e._l(e.spices,(function(r){return t("el-radio",{key:r,attrs:{label:r}},[e._v(e._s(r))])})),1)],1),t("el-form-item",{attrs:{label:"添加香菜"}},[t("el-radio-group",{model:{value:e.form.addCilantro,callback:function(t){e.$set(e.form,"addCilantro",t)},expression:"form.addCilantro"}},[t("el-radio",{attrs:{label:!0}},[e._v("是")]),t("el-radio",{attrs:{label:!1}},[e._v("否")])],1)],1),t("el-form-item",{attrs:{label:"添加葱花"}},[t("el-radio-group",{model:{value:e.form.addOnion,callback:function(t){e.$set(e.form,"addOnion",t)},expression:"form.addOnion"}},[t("el-radio",{attrs:{label:!0}},[e._v("是")]),t("el-radio",{attrs:{label:!1}},[e._v("否")])],1)],1)],1),t("div",{staticClass:"button-container"},[t("el-button",{staticClass:"center-button",attrs:{type:"primary",disabled:"red"===e.server.color},on:{click:e.submitOrder}},[e._v("提交订单")])],1)],1)],1)],1),t("div",{staticClass:"status-container",style:{color:e.server.color}},[t("span",[e._v("设备连接状态: "+e._s(e.server.message))])])])},f=[],p=r(4373),h={name:"App",data(){return{form:{selectedRecipe:"牛肉汤",selectedPrice:20,selectedSpice:"微辣",addCilantro:!0,addOnion:!0},recipes:["牛肉汤","牛杂汤"],prices:[10,15,20],spices:["不辣","微辣","中辣","辣"],orderSubmitted:!1,server:{message:"获取中...",color:"black"}}},methods:{async submitOrder(){if(this.form.selectedRecipe&&this.form.selectedPrice&&this.form.selectedSpice)try{const e=await p.A.post("http://127.0.0.1:8080/orders",this.form,{headers:{"Content-Type":"application/json"}});this.orderSubmitted=!0,this.$message.success("订单提交成功"),console.log(e.data),setTimeout((()=>{location.reload()}),5e3)}catch(e){this.$message.error("订单提交失败"),console.error(e)}else this.$message.error("请完整选择所有选项")},async fetchServerStatus(){try{const e=await p.A.get("http://127.0.0.1:8080/orders/serverStatus");this.server=e.data,console.log("response.data==="+e.data)}catch(e){this.serverMessage="获取失败",this.serverColor="red",console.error(e)}}},mounted(){this.fetchServerStatus(),this.interval=setInterval(this.fetchServerStatus,1e3)},beforeUnmount(){clearInterval(this.interval)}},b=h,v=(0,l.A)(b,m,f,!1,null,null,null),g=v.exports,y=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticClass:"container"},[t("div",{staticClass:"button-column"},e._l(e.buttonColumns[0],(function(r){return t("el-button",{key:r.id,attrs:{type:"primary"},on:{click:function(t){return e.openDialog(r.id,r.name)}}},[e._v(" "+e._s(r.name)+" ")])})),1),t("div",{staticClass:"button-column"},e._l(e.buttonColumns[1],(function(r){return t("el-button",{key:r.id,attrs:{type:"primary"},on:{click:function(t){return e.openDialog(r.id,r.name)}}},[e._v(" "+e._s(r.name)+" ")])})),1)]),t("div",{staticClass:"fixed-buttons"},[t("el-button",{staticClass:"reset-button",attrs:{type:"success"},on:{click:e.resetSystem}},[e._v("复位")]),t("el-button",{staticClass:"emergency-button",attrs:{type:"danger"},on:{click:e.emergencyStop}},[e._v("急停")])],1),t("el-dialog",{attrs:{title:"输入参数",visible:e.dialogVisible,"before-close":e.handleClose},scopedSlots:e._u([{key:"footer",fn:function(){return[t("el-button",{on:{click:function(t){e.dialogVisible=!1}}},[e._v("取消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.submitParameter}},[e._v("确定")])]},proxy:!0}])},[t("el-input",{attrs:{placeholder:"请输入参数"},model:{value:e.parameter,callback:function(t){e.parameter=t},expression:"parameter"}})],1)],1)},_=[];const k="http://127.0.0.1:8080/buttonAction";var C={name:"ButtonsPage",data(){return{buttons:[{id:1,name:"机器人重置"},{id:2,name:"机器人取碗"},{id:3,name:"机器人出汤"},{id:4,name:"取餐口复位"},{id:5,name:"取餐口出餐"},{id:6,name:"调料机测试（配方）"},{id:7,name:"转台复位"},{id:8,name:"转台工位（数字）"},{id:9,name:"碗复位"},{id:10,name:"碗向上"},{id:11,name:"碗向下"},{id:12,name:"抽汤泵（秒）"},{id:13,name:"汤开关开"},{id:14,name:"汤开关关"},{id:15,name:"后箱风扇开"},{id:16,name:"后箱风扇关"},{id:17,name:"震动器测试（秒）"},{id:18,name:"蒸汽打开"},{id:19,name:"蒸汽关闭"},{id:20,name:"碗加蒸汽蒸汽（秒）"},{id:21,name:"汤加热至（度）"},{id:22,name:"弹簧货道（编号）"},{id:23,name:"配菜称重盒打开（编号）"},{id:24,name:"配菜称重盒关闭（编号）"},{id:25,name:"1号配菜电机（g）"},{id:26,name:"2号配菜电机（g）"},{id:27,name:"3号配菜电机（g）"}],buttonColumns:[],dialogVisible:!1,parameter:"",currentButtonId:null,currentButtonName:""}},created(){this.buttonColumns=this.chunkArray(this.buttons,Math.ceil(this.buttons.length/2))},methods:{async sendRequest(e){try{const t=await p.A.get(e);this.$message.success(`操作成功：${t.data}`),console.log(t.data)}catch(t){this.$message.error("操作失败"),console.error(t)}},openDialog(e,t){[6,8,12,17,21,22,23,24,25,26,27].includes(e)?this.dialogVisible=!0:(this.dialogVisible=!1,this.sendRequest(`${k}/${e}`)),this.currentButtonId=e,this.currentButtonName=t},handleClose(){this.dialogVisible=!1},submitParameter(){let e=`${k}/${this.currentButtonId}`;this.parameter&&(e+=`?number=${this.parameter}`),this.sendRequest(e),this.dialogVisible=!1,this.parameter=""},emergencyStop(){this.sendRequest(`${k}/emergencyStop`)},resetSystem(){this.sendRequest(`${k}/reset`)},chunkArray(e,t){return Array.from({length:Math.ceil(e.length/t)},((r,n)=>e.slice(n*t,n*t+t)))}}},S=C,O=(0,l.A)(S,y,_,!1,null,"18a2075e",null),$=O.exports;n["default"].use(d.Ay);var x=new d.Ay({mode:"hash",routes:[{path:"/",name:"OrderPage",component:g},{path:"/buttons",name:"ButtonsPage",component:$}]}),w=r(9143),A=r.n(w);n["default"].use(A()),new n["default"]({router:x,render:e=>e(u)}).$mount("#app")}},t={};function r(n){var o=t[n];if(void 0!==o)return o.exports;var a=t[n]={id:n,loaded:!1,exports:{}};return e[n].call(a.exports,a,a.exports,r),a.loaded=!0,a.exports}r.m=e,function(){r.amdO={}}(),function(){var e=[];r.O=function(t,n,o,a){if(!n){var i=1/0;for(u=0;u<e.length;u++){n=e[u][0],o=e[u][1],a=e[u][2];for(var s=!0,l=0;l<n.length;l++)(!1&a||i>=a)&&Object.keys(r.O).every((function(e){return r.O[e](n[l])}))?n.splice(l--,1):(s=!1,a<i&&(i=a));if(s){e.splice(u--,1);var c=o();void 0!==c&&(t=c)}}return t}a=a||0;for(var u=e.length;u>0&&e[u-1][2]>a;u--)e[u]=e[u-1];e[u]=[n,o,a]}}(),function(){r.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return r.d(t,{a:t}),t}}(),function(){r.d=function(e,t){for(var n in t)r.o(t,n)&&!r.o(e,n)&&Object.defineProperty(e,n,{enumerable:!0,get:t[n]})}}(),function(){r.g=function(){if("object"===typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(e){if("object"===typeof window)return window}}()}(),function(){r.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)}}(),function(){r.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})}}(),function(){r.nmd=function(e){return e.paths=[],e.children||(e.children=[]),e}}(),function(){var e={524:0};r.O.j=function(t){return 0===e[t]};var t=function(t,n){var o,a,i=n[0],s=n[1],l=n[2],c=0;if(i.some((function(t){return 0!==e[t]}))){for(o in s)r.o(s,o)&&(r.m[o]=s[o]);if(l)var u=l(r)}for(t&&t(n);c<i.length;c++)a=i[c],r.o(e,a)&&e[a]&&e[a][0](),e[a]=0;return r.O(u)},n=self["webpackChunkfrontend"]=self["webpackChunkfrontend"]||[];n.forEach(t.bind(null,0)),n.push=t.bind(null,n.push.bind(n))}();var n=r.O(void 0,[504],(function(){return r(8804)}));n=r.O(n)})();
//# sourceMappingURL=app.820097d3.js.map