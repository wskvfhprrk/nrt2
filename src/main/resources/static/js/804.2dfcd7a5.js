"use strict";(self["webpackChunkvue"]=self["webpackChunkvue"]||[]).push([[804],{7804:(n,t,e)=>{e.r(t),e.d(t,{default:()=>A});e(2010);var o=e(641),r=e(33),u=function(n){return(0,o.Qi)("data-v-cf8d4d12"),n=n(),(0,o.jt)(),n},i={class:"container"},a={class:"button-group"},c=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"机器人和取餐",-1)})),s={class:"button-columns"},l={class:"button-group"},d=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"碗粉丝",-1)})),m={class:"button-columns"},p={class:"button-group"},f=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"蒸汽和温度",-1)})),b={class:"button-columns"},k={class:"button-group"},v=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"风扇和称重",-1)})),y={class:"button-columns"},g={class:"button-group"},h=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"配料",-1)})),C={class:"button-columns"},L={class:"button-group"},_=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"柜门",-1)})),F={class:"button-columns"},x={class:"fixed-buttons"};function E(n,t,e,u,E,S){var V=(0,o.g2)("el-button"),X=(0,o.g2)("el-input"),G=(0,o.g2)("el-dialog");return(0,o.uX)(),(0,o.CE)("div",i,[(0,o.Lk)("div",a,[c,(0,o.Lk)("div",s,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup1,(function(n,t){return(0,o.uX)(),(0,o.CE)("div",{key:"btn1-".concat(t),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(t){return S.openDialog(n.id,n.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(n.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",l,[d,(0,o.Lk)("div",m,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup2,(function(n,t){return(0,o.uX)(),(0,o.CE)("div",{key:"btn2-".concat(t),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(t){return S.openDialog(n.id,n.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(n.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",p,[f,(0,o.Lk)("div",b,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup3,(function(n,t){return(0,o.uX)(),(0,o.CE)("div",{key:"btn3-".concat(t),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(t){return S.openDialog(n.id,n.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(n.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",k,[v,(0,o.Lk)("div",y,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup4,(function(n,t){return(0,o.uX)(),(0,o.CE)("div",{key:"btn4-".concat(t),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(t){return S.openDialog(n.id,n.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(n.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",g,[h,(0,o.Lk)("div",C,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup5,(function(n,t){return(0,o.uX)(),(0,o.CE)("div",{key:"btn5-".concat(t),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(t){return S.openDialog(n.id,n.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(n.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",L,[_,(0,o.Lk)("div",F,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup6,(function(n,t){return(0,o.uX)(),(0,o.CE)("div",{key:"btn6-".concat(t),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(t){return S.openDialog(n.id,n.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(n.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",x,[(0,o.bF)(V,{type:"success",class:"reset-button",onClick:S.resetSystem},{default:(0,o.k6)((function(){return[(0,o.eW)("复位")]})),_:1},8,["onClick"]),(0,o.bF)(V,{type:"danger",class:"emergency-button",onClick:S.emergencyStop},{default:(0,o.k6)((function(){return[(0,o.eW)("急停")]})),_:1},8,["onClick"])]),(0,o.bF)(G,{title:"输入参数",modelValue:E.dialogVisible,"onUpdate:modelValue":t[2]||(t[2]=function(n){return E.dialogVisible=n}),"before-close":S.handleClose},{footer:(0,o.k6)((function(){return[(0,o.bF)(V,{onClick:t[1]||(t[1]=function(n){return E.dialogVisible=!1})},{default:(0,o.k6)((function(){return[(0,o.eW)("取消")]})),_:1}),(0,o.bF)(V,{type:"primary",onClick:S.submitParameter},{default:(0,o.k6)((function(){return[(0,o.eW)("确定")]})),_:1},8,["onClick"])]})),default:(0,o.k6)((function(){return[(0,o.bF)(X,{modelValue:E.parameter,"onUpdate:modelValue":t[0]||(t[0]=function(n){return E.parameter=n}),placeholder:"请输入参数"},null,8,["modelValue"])]})),_:1},8,["modelValue","before-close"])])}var S=e(459),V=e(388),X=(e(8706),e(4423),e(1699),e(2505)),G=e.n(X),I="http://127.0.0.1:8080/buttonAction";const W={name:"ManualOperation",data:function(){return{buttonsGroup1:[{id:1,name:"机器人重置"},{id:2,name:"机器人取粉丝"},{id:3,name:"机器人取菜蓝"},{id:4,name:"机器人取碗"},{id:5,name:"机器人放碗"},{id:6,name:"机器人出餐"},{id:7,name:"取餐口复位"},{id:8,name:"取餐口出餐"}],buttonsGroup2:[{id:9,name:"出碗"},{id:10,name:"粉丝仓复位"},{id:11,name:"移动粉丝仓（编号）"},{id:12,name:"粉丝仓出粉丝"},{id:13,name:"装菜勺复位"},{id:14,name:"装菜勺倒菜"},{id:15,name:"装菜勺装菜"}],buttonsGroup3:[{id:16,name:"加蒸汽盖下降"},{id:17,name:"加汤盖下降"},{id:18,name:"加汤蒸汤盖上升"},{id:19,name:"加汤（秒）"},{id:20,name:"汤管排气（秒）"},{id:21,name:"汤加热至（度）"},{id:22,name:"加蒸汽（秒）"}],buttonsGroup4:[{id:23,name:"后箱风扇开"},{id:24,name:"后箱风扇关"},{id:25,name:"配菜（g）"},{id:27,name:"称重清0"},{id:28,name:"标重500g"}],buttonsGroup5:[{id:31,name:"切肉机切肉（份量）"},{id:32,name:"打开震动器料仓"},{id:36,name:"关闭震动器料仓"},{id:37,name:"打开称重盒"},{id:38,name:"关闭称重盒"}],buttonsGroup6:[{id:40,name:"左上打开"},{id:42,name:"左下打开"},{id:44,name:"中间左上打开"},{id:46,name:"中间左下打开"},{id:48,name:"中间右上打开"},{id:50,name:"中间右下打开"}],dialogVisible:!1,parameter:"",currentButtonId:null,currentButtonName:"",errorHandler:null}},computed:{buttonStyle:function(){return{width:"200px"}}},methods:{sendRequest:function(n){var t=this;return(0,V.A)((0,S.A)().mark((function e(){var o;return(0,S.A)().wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.prev=0,e.next=3,G().get(n);case 3:o=e.sent,200===o.data.code?t.$message.success("".concat(o.data.data)):t.$message.error("操作失败：".concat(o.data.message)),console.log(o.data),e.next=12;break;case 8:e.prev=8,e.t0=e["catch"](0),t.$message.error("操作失败"),console.error(e.t0);case 12:case"end":return e.stop()}}),e,null,[[0,8]])})))()},openDialog:function(n,t){t.includes("（")&&t.includes("）")?this.dialogVisible=!0:(this.dialogVisible=!1,this.sendRequest("".concat(I,"/").concat(n))),this.currentButtonId=n,this.currentButtonName=t},handleClose:function(){this.dialogVisible=!1},submitParameter:function(){var n="".concat(I,"/").concat(this.currentButtonId,"?number=").concat(this.parameter);this.sendRequest(n),this.dialogVisible=!1,this.parameter=""},emergencyStop:function(){this.sendRequest("".concat(I,"/emergencyStop"))},resetSystem:function(){this.sendRequest("".concat(I,"/reset"))}}};var w=e(6262);const T=(0,w.A)(W,[["render",E],["__scopeId","data-v-cf8d4d12"]]),A=T},597:(n,t,e)=>{var o=e(9039),r=e(8227),u=e(9519),i=r("species");n.exports=function(n){return u>=51||!o((function(){var t=[],e=t.constructor={};return e[i]=function(){return{foo:1}},1!==t[n](Boolean).foo}))}},7433:(n,t,e)=>{var o=e(4376),r=e(3517),u=e(34),i=e(8227),a=i("species"),c=Array;n.exports=function(n){var t;return o(n)&&(t=n.constructor,r(t)&&(t===c||o(t.prototype))?t=void 0:u(t)&&(t=t[a],null===t&&(t=void 0))),void 0===t?c:t}},1469:(n,t,e)=>{var o=e(7433);n.exports=function(n,t){return new(o(n))(0===t?0:t)}},1436:(n,t,e)=>{var o=e(8227),r=o("match");n.exports=function(n){var t=/./;try{"/./"[n](t)}catch(e){try{return t[r]=!1,"/./"[n](t)}catch(o){}}return!1}},4659:(n,t,e)=>{var o=e(3724),r=e(4913),u=e(6980);n.exports=function(n,t,e){o?r.f(n,t,u(0,e)):n[t]=e}},788:(n,t,e)=>{var o=e(34),r=e(4576),u=e(8227),i=u("match");n.exports=function(n){var t;return o(n)&&(void 0!==(t=n[i])?!!t:"RegExp"===r(n))}},511:(n,t,e)=>{var o=e(788),r=TypeError;n.exports=function(n){if(o(n))throw new r("The method doesn't accept regular expressions");return n}},8706:(n,t,e)=>{var o=e(6518),r=e(9039),u=e(4376),i=e(34),a=e(8981),c=e(6198),s=e(6837),l=e(4659),d=e(1469),m=e(597),p=e(8227),f=e(9519),b=p("isConcatSpreadable"),k=f>=51||!r((function(){var n=[];return n[b]=!1,n.concat()[0]!==n})),v=function(n){if(!i(n))return!1;var t=n[b];return void 0!==t?!!t:u(n)},y=!k||!m("concat");o({target:"Array",proto:!0,arity:1,forced:y},{concat:function(n){var t,e,o,r,u,i=a(this),m=d(i,0),p=0;for(t=-1,o=arguments.length;t<o;t++)if(u=-1===t?i:arguments[t],v(u))for(r=c(u),s(p+r),e=0;e<r;e++,p++)e in u&&l(m,p,u[e]);else s(p+1),l(m,p++,u);return m.length=p,m}})},1699:(n,t,e)=>{var o=e(6518),r=e(9504),u=e(511),i=e(7750),a=e(655),c=e(1436),s=r("".indexOf);o({target:"String",proto:!0,forced:!c("includes")},{includes:function(n){return!!~s(a(i(this)),a(u(n)),arguments.length>1?arguments[1]:void 0)}})}}]);
//# sourceMappingURL=804.2dfcd7a5.js.map