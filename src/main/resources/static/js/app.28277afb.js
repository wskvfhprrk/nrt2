(()=>{"use strict";var e={7087:(e,t,r)=>{r(3792),r(3362),r(9085),r(9391);var n=r(3751),a=r(641),o={id:"app"};function u(e,t,r,n,u,s){var l=(0,a.g2)("router-view");return(0,a.uX)(),(0,a.CE)("div",o,[(0,a.bF)(l)])}const s={name:"App"};var l=r(6262);const i=(0,l.A)(s,[["render",u],["__scopeId","data-v-42dc5978"]]),c=i;var d=r(5220),m=r(33),f={id:"app",class:"outer-container"},p={key:0,class:"order-status-display"},b={key:0,class:"order-status pending"},v={class:"order-list"},h={key:1,class:"order-status in-progress"},g={class:"order-list"},k={key:2,class:"order-status completed"},_={class:"order-list"},O={class:"container"},y={class:"button-container"};function C(e,t,r,n,o,u){var s=(0,a.g2)("el-radio"),l=(0,a.g2)("el-radio-group"),i=(0,a.g2)("el-form-item"),c=(0,a.g2)("el-form"),d=(0,a.g2)("el-button"),C=(0,a.g2)("el-main"),F=(0,a.g2)("el-container");return(0,a.uX)(),(0,a.CE)("div",f,[o.hasOrders?((0,a.uX)(),(0,a.CE)("div",p,[o.pendingOrders.length?((0,a.uX)(),(0,a.CE)("div",b,[(0,a.eW)(" 待做订单: "),(0,a.Lk)("span",null,(0,m.v_)(o.pendingOrders.length),1),(0,a.Lk)("div",v,[((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(o.pendingOrders,(function(e){return(0,a.uX)(),(0,a.CE)("div",{key:e.orderId,class:"customer-name"},(0,m.v_)(e.customerName),1)})),128))])])):(0,a.Q3)("",!0),o.inProgressOrders.length?((0,a.uX)(),(0,a.CE)("div",h,[(0,a.eW)(" 在做订单: "),(0,a.Lk)("span",null,(0,m.v_)(o.inProgressOrders.length),1),(0,a.Lk)("div",g,[((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(o.inProgressOrders,(function(e){return(0,a.uX)(),(0,a.CE)("div",{key:e.orderId,class:"customer-name"},(0,m.v_)(e.customerName),1)})),128))])])):(0,a.Q3)("",!0),o.completedOrders.length?((0,a.uX)(),(0,a.CE)("div",k,[(0,a.eW)(" 做完订单: "),(0,a.Lk)("span",null,(0,m.v_)(o.completedOrders.length),1),(0,a.Lk)("div",_,[((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(o.completedOrders,(function(e){return(0,a.uX)(),(0,a.CE)("div",{key:e.orderId,class:"customer-name"},(0,m.v_)(e.customerName),1)})),128))])])):(0,a.Q3)("",!0)])):(0,a.Q3)("",!0),(0,a.Lk)("div",O,[(0,a.bF)(F,null,{default:(0,a.k6)((function(){return[(0,a.bF)(C,null,{default:(0,a.k6)((function(){return[(0,a.bF)(c,{model:o.form,"label-width":"120px"},{default:(0,a.k6)((function(){return[(0,a.bF)(i,{label:"选择食谱"},{default:(0,a.k6)((function(){return[(0,a.bF)(l,{modelValue:o.form.selectedRecipe,"onUpdate:modelValue":t[0]||(t[0]=function(e){return o.form.selectedRecipe=e})},{default:(0,a.k6)((function(){return[(0,a.bF)(s,{label:"牛肉汤"},{default:(0,a.k6)((function(){return[(0,a.eW)("牛肉汤")]})),_:1}),(0,a.bF)(s,{label:"牛杂汤"},{default:(0,a.k6)((function(){return[(0,a.eW)("牛杂汤")]})),_:1})]})),_:1},8,["modelValue"])]})),_:1}),(0,a.bF)(i,{label:"选择类别"},{default:(0,a.k6)((function(){return[(0,a.bF)(l,{modelValue:o.form.selectedPrice,"onUpdate:modelValue":t[1]||(t[1]=function(e){return o.form.selectedPrice=e})},{default:(0,a.k6)((function(){return[((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(o.prices,(function(e){return(0,a.uX)(),(0,a.Wv)(s,{key:e,label:e},{default:(0,a.k6)((function(){return[(0,a.eW)((0,m.v_)(e)+"元",1)]})),_:2},1032,["label"])})),128))]})),_:1},8,["modelValue"])]})),_:1}),(0,a.bF)(i,{label:"选择口味"},{default:(0,a.k6)((function(){return[(0,a.bF)(l,{modelValue:o.form.selectedSpice,"onUpdate:modelValue":t[2]||(t[2]=function(e){return o.form.selectedSpice=e})},{default:(0,a.k6)((function(){return[((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(o.spices,(function(e){return(0,a.uX)(),(0,a.Wv)(s,{key:e,label:e},{default:(0,a.k6)((function(){return[(0,a.eW)((0,m.v_)(e),1)]})),_:2},1032,["label"])})),128))]})),_:1},8,["modelValue"])]})),_:1}),(0,a.bF)(i,{label:"添加香菜"},{default:(0,a.k6)((function(){return[(0,a.bF)(l,{modelValue:o.form.addCilantro,"onUpdate:modelValue":t[3]||(t[3]=function(e){return o.form.addCilantro=e})},{default:(0,a.k6)((function(){return[(0,a.bF)(s,{label:!0},{default:(0,a.k6)((function(){return[(0,a.eW)("是")]})),_:1}),(0,a.bF)(s,{label:!1},{default:(0,a.k6)((function(){return[(0,a.eW)("否")]})),_:1})]})),_:1},8,["modelValue"])]})),_:1}),(0,a.bF)(i,{label:"添加葱花"},{default:(0,a.k6)((function(){return[(0,a.bF)(l,{modelValue:o.form.addOnion,"onUpdate:modelValue":t[4]||(t[4]=function(e){return o.form.addOnion=e})},{default:(0,a.k6)((function(){return[(0,a.bF)(s,{label:!0},{default:(0,a.k6)((function(){return[(0,a.eW)("是")]})),_:1}),(0,a.bF)(s,{label:!1},{default:(0,a.k6)((function(){return[(0,a.eW)("否")]})),_:1})]})),_:1},8,["modelValue"])]})),_:1})]})),_:1},8,["model"]),(0,a.Lk)("div",y,[(0,a.bF)(d,{type:"primary",disabled:!o.isButtonEnabled,onClick:u.submitOrder,class:"center-button"},{default:(0,a.k6)((function(){return[(0,a.eW)("提交订单 ")]})),_:1},8,["disabled","onClick"])])]})),_:1})]})),_:1})]),(0,a.Lk)("div",{class:"status-message",style:(0,m.Tr)({color:o.serverStatus.color})},(0,m.v_)(o.serverStatus.message||"默认状态信息显示"),5)])}var F=r(459),V=r(388),E=(r(6031),r(2505)),S=r.n(E);const w={name:"App",data:function(){return{form:{selectedRecipe:"牛肉汤",selectedPrice:20,selectedSpice:"微辣",addCilantro:!0,addOnion:!0},recipes:["牛肉汤","牛杂汤"],prices:[10,15,20],spices:["不辣","微辣","中辣","辣"],orderSubmitted:!1,isButtonEnabled:!1,serverStatus:{color:"black",message:""},pendingOrders:[],inProgressOrders:[],completedOrders:[],hasOrders:!1}},methods:{submitOrder:function(){var e=this;return(0,V.A)((0,F.A)().mark((function t(){var r;return(0,F.A)().wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(!(e.form.selectedRecipe&&e.form.selectedPrice&&e.form.selectedSpice)){t.next=17;break}return t.prev=1,t.next=4,S().post("http://127.0.0.1:8080/orders",e.form,{headers:{"Content-Type":"application/json"}});case 4:r=t.sent,e.orderSubmitted=!0,e.$message.success("订单提交成功"),console.log(r.data),setTimeout((function(){location.reload()}),5e3),t.next=15;break;case 11:t.prev=11,t.t0=t["catch"](1),e.$message.error("订单提交失败"),console.error(t.t0);case 15:t.next=18;break;case 17:e.$message.error("请完整选择所有选项");case 18:case"end":return t.stop()}}),t,null,[[1,11]])})))()},fetchServerStatus:function(){var e=this;return(0,V.A)((0,F.A)().mark((function t(){var r;return(0,F.A)().wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,S().get("http://127.0.0.1:8080/orders/serverStatus");case 3:r=t.sent,e.serverStatus.color=r.data.color,e.serverStatus.message=r.data.message,e.isButtonEnabled="green"===e.serverStatus.color,t.next=13;break;case 9:t.prev=9,t.t0=t["catch"](0),console.error("获取服务器状态时出错:",t.t0),e.isButtonEnabled=!1;case 13:case"end":return t.stop()}}),t,null,[[0,9]])})))()},fetchOrderData:function(){var e=this;return(0,V.A)((0,F.A)().mark((function t(){var r;return(0,F.A)().wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,S().get("http://127.0.0.1:8080/orders/status");case 3:r=t.sent,200===r.data.code?(e.pendingOrders=r.data.data.pendingOrders||[],e.inProgressOrders=r.data.data.inProgressOrders||[],e.completedOrders=r.data.data.completedOrders||[],e.hasOrders=e.pendingOrders.length>0||e.inProgressOrders.length>0||e.completedOrders.length>0):console.error(r.data.message),t.next=10;break;case 7:t.prev=7,t.t0=t["catch"](0),console.error("获取订单数据时出错:",t.t0);case 10:case"end":return t.stop()}}),t,null,[[0,7]])})))()}},mounted:function(){this.fetchServerStatus(),setInterval(this.fetchServerStatus,1e3),this.fetchOrderData(),setInterval(this.fetchOrderData,1e3)}},A=(0,l.A)(w,[["render",C]]),x=A;r(2010);var X={class:"container"},P={class:"button-columns"},W={class:"fixed-buttons"};function I(e,t,r,n,o,u){var s=(0,a.g2)("el-button"),l=(0,a.g2)("el-input"),i=(0,a.g2)("el-dialog");return(0,a.uX)(),(0,a.CE)("div",null,[(0,a.Lk)("div",X,[(0,a.Lk)("div",P,[((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(o.buttonColumns,(function(e,t){return(0,a.uX)(),(0,a.CE)("div",{key:t,class:"button-column"},[((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(e,(function(e){return(0,a.uX)(),(0,a.Wv)(s,{key:e.id,type:"primary",onClick:function(t){return u.openDialog(e.id,e.name)}},{default:(0,a.k6)((function(){return[(0,a.eW)((0,m.v_)(e.name),1)]})),_:2},1032,["onClick"])})),128))])})),128))])]),(0,a.Lk)("div",W,[(0,a.bF)(s,{type:"success",class:"reset-button",onClick:u.resetSystem},{default:(0,a.k6)((function(){return[(0,a.eW)("复位")]})),_:1},8,["onClick"]),(0,a.bF)(s,{type:"danger",class:"emergency-button",onClick:u.emergencyStop},{default:(0,a.k6)((function(){return[(0,a.eW)("急停")]})),_:1},8,["onClick"])]),(0,a.bF)(i,{title:"输入参数",modelValue:o.dialogVisible,"onUpdate:modelValue":t[2]||(t[2]=function(e){return o.dialogVisible=e}),"before-close":u.handleClose},{footer:(0,a.k6)((function(){return[(0,a.bF)(s,{onClick:t[1]||(t[1]=function(e){return o.dialogVisible=!1})},{default:(0,a.k6)((function(){return[(0,a.eW)("取消")]})),_:1}),(0,a.bF)(s,{type:"primary",onClick:u.submitParameter},{default:(0,a.k6)((function(){return[(0,a.eW)("确定")]})),_:1},8,["onClick"])]})),default:(0,a.k6)((function(){return[(0,a.bF)(l,{modelValue:o.parameter,"onUpdate:modelValue":t[0]||(t[0]=function(e){return o.parameter=e}),placeholder:"请输入参数"},null,8,["modelValue"])]})),_:1},8,["modelValue","before-close"])])}r(8706),r(3418),r(4423),r(4782),r(7764);var B="http://127.0.0.1:8080/buttonAction";const L={name:"ButtonsPage",data:function(){return{buttons:[{id:1,name:"机器人重置"},{id:2,name:"机器人取碗"},{id:3,name:"机器人出汤"},{id:4,name:"取餐口复位"},{id:5,name:"取餐口出餐"},{id:6,name:"调料机测试（配方）"},{id:7,name:"转台复位"},{id:8,name:"转台工位（数字）"},{id:9,name:"碗复位"},{id:10,name:"碗向上"},{id:11,name:"碗向下"},{id:12,name:"抽汤泵（秒）"},{id:13,name:"汤开关开"},{id:14,name:"汤开关关"},{id:15,name:"后箱风扇开"},{id:16,name:"后箱风扇关"},{id:17,name:"震动器测试（秒）"},{id:18,name:"蒸汽打开"},{id:19,name:"蒸汽关闭"},{id:20,name:"碗加蒸汽（秒）"},{id:21,name:"汤加热温度"},{id:22,name:"弹簧货道（编号）"},{id:23,name:"配菜称重盒打开（编号）"},{id:24,name:"配菜称重盒关闭（编号）"},{id:25,name:"1号配菜电机（g）"},{id:26,name:"2号配菜电机（g）"},{id:27,name:"3号配菜电机（g）"}],buttonsPerColumn:14,buttonColumns:[],dialogVisible:!1,parameter:"",currentButtonId:null,currentButtonName:""}},created:function(){this.buttonColumns=this.chunkArray(this.buttons,this.buttonsPerColumn)},methods:{sendRequest:function(e){var t=this;return(0,V.A)((0,F.A)().mark((function r(){var n;return(0,F.A)().wrap((function(r){while(1)switch(r.prev=r.next){case 0:return r.prev=0,r.next=3,S().get(e);case 3:n=r.sent,t.$message.success("操作成功：".concat(n.data)),console.log(n.data),r.next=12;break;case 8:r.prev=8,r.t0=r["catch"](0),t.$message.error("操作失败"),console.error(r.t0);case 12:case"end":return r.stop()}}),r,null,[[0,8]])})))()},openDialog:function(e,t){[6,8,12,17,20,21,22,23,24,25,26,27].includes(e)?this.dialogVisible=!0:(this.dialogVisible=!1,this.sendRequest("".concat(B,"/").concat(e))),this.currentButtonId=e,this.currentButtonName=t},handleClose:function(){this.dialogVisible=!1},submitParameter:function(){var e="".concat(B,"/").concat(this.currentButtonId,"?number=").concat(this.parameter);this.sendRequest(e),this.dialogVisible=!1,this.parameter=""},emergencyStop:function(){this.sendRequest("".concat(B,"/emergencyStop"))},resetSystem:function(){this.sendRequest("".concat(B,"/reset"))},chunkArray:function(e,t){return Array.from({length:Math.ceil(e.length/t)},(function(r,n){return e.slice(n*t,n*t+t)}))}}},R=(0,l.A)(L,[["render",I],["__scopeId","data-v-53195425"]]),j=R;var K=[{path:"/",name:"OrderPage",component:x},{path:"/buttons",name:"ButtonsPage",component:j}],U=(0,d.aE)({history:(0,d.Bt)(),routes:K});const q=U;var D=r(5484);r(4188);(0,n.Ef)(c).use(q).use(D.A).mount("#app")}},t={};function r(n){var a=t[n];if(void 0!==a)return a.exports;var o=t[n]={exports:{}};return e[n].call(o.exports,o,o.exports,r),o.exports}r.m=e,(()=>{var e=[];r.O=(t,n,a,o)=>{if(!n){var u=1/0;for(c=0;c<e.length;c++){for(var[n,a,o]=e[c],s=!0,l=0;l<n.length;l++)(!1&o||u>=o)&&Object.keys(r.O).every((e=>r.O[e](n[l])))?n.splice(l--,1):(s=!1,o<u&&(u=o));if(s){e.splice(c--,1);var i=a();void 0!==i&&(t=i)}}return t}o=o||0;for(var c=e.length;c>0&&e[c-1][2]>o;c--)e[c]=e[c-1];e[c]=[n,a,o]}})(),(()=>{r.n=e=>{var t=e&&e.__esModule?()=>e["default"]:()=>e;return r.d(t,{a:t}),t}})(),(()=>{r.d=(e,t)=>{for(var n in t)r.o(t,n)&&!r.o(e,n)&&Object.defineProperty(e,n,{enumerable:!0,get:t[n]})}})(),(()=>{r.g=function(){if("object"===typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(e){if("object"===typeof window)return window}}()})(),(()=>{r.o=(e,t)=>Object.prototype.hasOwnProperty.call(e,t)})(),(()=>{var e={524:0};r.O.j=t=>0===e[t];var t=(t,n)=>{var a,o,[u,s,l]=n,i=0;if(u.some((t=>0!==e[t]))){for(a in s)r.o(s,a)&&(r.m[a]=s[a]);if(l)var c=l(r)}for(t&&t(n);i<u.length;i++)o=u[i],r.o(e,o)&&e[o]&&e[o][0](),e[o]=0;return r.O(c)},n=self["webpackChunkmy_vue_app"]=self["webpackChunkmy_vue_app"]||[];n.forEach(t.bind(null,0)),n.push=t.bind(null,n.push.bind(n))})();var n=r.O(void 0,[504],(()=>r(7087)));n=r.O(n)})();
//# sourceMappingURL=app.28277afb.js.map