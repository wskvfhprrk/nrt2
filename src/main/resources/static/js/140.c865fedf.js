"use strict";(self["webpackChunkvue"]=self["webpackChunkvue"]||[]).push([[140],{4140:(t,n,e)=>{e.r(n),e.d(n,{default:()=>A});e(2010);var o=e(641),r=e(33),u=function(t){return(0,o.Qi)("data-v-35cfc8ab"),t=t(),(0,o.jt)(),t},i={class:"container"},c={class:"button-group"},a=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"机器人和取餐",-1)})),s={class:"button-columns"},l={class:"button-group"},d=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"碗粉丝",-1)})),m={class:"button-columns"},p={class:"button-group"},f=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"蒸汽和温度",-1)})),b={class:"button-columns"},k={class:"button-group"},v=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"风扇和振动",-1)})),y={class:"button-columns"},g={class:"button-group"},h=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"称重",-1)})),C={class:"button-columns"},L={class:"button-group"},_=u((function(){return(0,o.Lk)("h3",{class:"section-title"},"配料分发",-1)})),F={class:"button-columns"},x={class:"fixed-buttons"};function E(t,n,e,u,E,S){var V=(0,o.g2)("el-button"),X=(0,o.g2)("el-input"),G=(0,o.g2)("el-dialog");return(0,o.uX)(),(0,o.CE)("div",i,[(0,o.Lk)("div",c,[a,(0,o.Lk)("div",s,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup1,(function(t,n){return(0,o.uX)(),(0,o.CE)("div",{key:"btn1-".concat(n),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(n){return S.openDialog(t.id,t.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(t.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",l,[d,(0,o.Lk)("div",m,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup2,(function(t,n){return(0,o.uX)(),(0,o.CE)("div",{key:"btn2-".concat(n),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(n){return S.openDialog(t.id,t.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(t.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",p,[f,(0,o.Lk)("div",b,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup3,(function(t,n){return(0,o.uX)(),(0,o.CE)("div",{key:"btn3-".concat(n),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(n){return S.openDialog(t.id,t.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(t.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",k,[v,(0,o.Lk)("div",y,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup4,(function(t,n){return(0,o.uX)(),(0,o.CE)("div",{key:"btn4-".concat(n),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(n){return S.openDialog(t.id,t.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(t.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",g,[h,(0,o.Lk)("div",C,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup5,(function(t,n){return(0,o.uX)(),(0,o.CE)("div",{key:"btn5-".concat(n),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(n){return S.openDialog(t.id,t.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(t.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",L,[_,(0,o.Lk)("div",F,[((0,o.uX)(!0),(0,o.CE)(o.FK,null,(0,o.pI)(E.buttonsGroup6,(function(t,n){return(0,o.uX)(),(0,o.CE)("div",{key:"btn6-".concat(n),class:"button-column"},[(0,o.bF)(V,{type:"primary",style:(0,r.Tr)(S.buttonStyle),onClick:function(n){return S.openDialog(t.id,t.name)}},{default:(0,o.k6)((function(){return[(0,o.eW)((0,r.v_)(t.name),1)]})),_:2},1032,["style","onClick"])])})),128))])]),(0,o.Lk)("div",x,[(0,o.bF)(V,{type:"success",class:"reset-button",onClick:S.resetSystem},{default:(0,o.k6)((function(){return[(0,o.eW)("复位")]})),_:1},8,["onClick"]),(0,o.bF)(V,{type:"danger",class:"emergency-button",onClick:S.emergencyStop},{default:(0,o.k6)((function(){return[(0,o.eW)("急停")]})),_:1},8,["onClick"])]),(0,o.bF)(G,{title:"输入参数",modelValue:E.dialogVisible,"onUpdate:modelValue":n[2]||(n[2]=function(t){return E.dialogVisible=t}),"before-close":S.handleClose},{footer:(0,o.k6)((function(){return[(0,o.bF)(V,{onClick:n[1]||(n[1]=function(t){return E.dialogVisible=!1})},{default:(0,o.k6)((function(){return[(0,o.eW)("取消")]})),_:1}),(0,o.bF)(V,{type:"primary",onClick:S.submitParameter},{default:(0,o.k6)((function(){return[(0,o.eW)("确定")]})),_:1},8,["onClick"])]})),default:(0,o.k6)((function(){return[(0,o.bF)(X,{modelValue:E.parameter,"onUpdate:modelValue":n[0]||(n[0]=function(t){return E.parameter=t}),placeholder:"请输入参数"},null,8,["modelValue"])]})),_:1},8,["modelValue","before-close"])])}var S=e(459),V=e(388),X=(e(8706),e(4423),e(1699),e(2505)),G=e.n(X),I="http://127.0.0.1:8080/buttonAction";const W={name:"ManualOperation",data:function(){return{buttonsGroup1:[{id:1,name:"机器人重置"},{id:2,name:"机器人取碗"},{id:3,name:"机器人取粉丝"},{id:4,name:"机器人出餐"},{id:6,name:"取餐口出餐"},{id:5,name:"取餐口复位"}],buttonsGroup2:[{id:7,name:"出碗"},{id:9,name:"粉丝仓出粉丝"},{id:8,name:"粉丝仓复位"}],buttonsGroup3:[{id:11,name:"蒸汽打开"},{id:12,name:"蒸汽关闭"},{id:13,name:"关汤蒸汽阀"},{id:14,name:"加汤（秒）"},{id:15,name:"汤管排气（秒）"},{id:17,name:"加蒸汽（秒）"},{id:16,name:"汤加热至（度）"}],buttonsGroup4:[{id:18,name:"后箱风扇开"},{id:19,name:"后箱风扇关"},{id:20,name:"震动器（秒）"}],buttonsGroup5:[{id:21,name:"一号配菜（g）"},{id:22,name:"二号配菜（g）"},{id:23,name:"三号配菜（g）"}],buttonsGroup6:[{id:24,name:"调料机（配方）"},{id:25,name:"弹簧货道（编号）"},{id:26,name:"称重盒开（编号）"},{id:27,name:"称重盒关（编号）"}],dialogVisible:!1,parameter:"",currentButtonId:null,currentButtonName:"",errorHandler:null}},computed:{buttonStyle:function(){return{width:"200px"}}},methods:{sendRequest:function(t){var n=this;return(0,V.A)((0,S.A)().mark((function e(){var o;return(0,S.A)().wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.prev=0,e.next=3,G().get(t);case 3:o=e.sent,n.$message.success("操作成功：".concat(o.data)),console.log(o.data),e.next=12;break;case 8:e.prev=8,e.t0=e["catch"](0),n.$message.error("操作失败"),console.error(e.t0);case 12:case"end":return e.stop()}}),e,null,[[0,8]])})))()},openDialog:function(t,n){n.includes("（")&&n.includes("）")?this.dialogVisible=!0:(this.dialogVisible=!1,this.sendRequest("".concat(I,"/").concat(t))),this.currentButtonId=t,this.currentButtonName=n},handleClose:function(){this.dialogVisible=!1},submitParameter:function(){var t="".concat(I,"/").concat(this.currentButtonId,"?number=").concat(this.parameter);this.sendRequest(t),this.dialogVisible=!1,this.parameter=""},emergencyStop:function(){this.sendRequest("".concat(I,"/emergencyStop"))},resetSystem:function(){this.sendRequest("".concat(I,"/reset"))}}};var w=e(6262);const T=(0,w.A)(W,[["render",E],["__scopeId","data-v-35cfc8ab"]]),A=T},597:(t,n,e)=>{var o=e(9039),r=e(8227),u=e(9519),i=r("species");t.exports=function(t){return u>=51||!o((function(){var n=[],e=n.constructor={};return e[i]=function(){return{foo:1}},1!==n[t](Boolean).foo}))}},7433:(t,n,e)=>{var o=e(4376),r=e(3517),u=e(34),i=e(8227),c=i("species"),a=Array;t.exports=function(t){var n;return o(t)&&(n=t.constructor,r(n)&&(n===a||o(n.prototype))?n=void 0:u(n)&&(n=n[c],null===n&&(n=void 0))),void 0===n?a:n}},1469:(t,n,e)=>{var o=e(7433);t.exports=function(t,n){return new(o(t))(0===n?0:n)}},1436:(t,n,e)=>{var o=e(8227),r=o("match");t.exports=function(t){var n=/./;try{"/./"[t](n)}catch(e){try{return n[r]=!1,"/./"[t](n)}catch(o){}}return!1}},4659:(t,n,e)=>{var o=e(3724),r=e(4913),u=e(6980);t.exports=function(t,n,e){o?r.f(t,n,u(0,e)):t[n]=e}},788:(t,n,e)=>{var o=e(34),r=e(4576),u=e(8227),i=u("match");t.exports=function(t){var n;return o(t)&&(void 0!==(n=t[i])?!!n:"RegExp"===r(t))}},511:(t,n,e)=>{var o=e(788),r=TypeError;t.exports=function(t){if(o(t))throw new r("The method doesn't accept regular expressions");return t}},8706:(t,n,e)=>{var o=e(6518),r=e(9039),u=e(4376),i=e(34),c=e(8981),a=e(6198),s=e(6837),l=e(4659),d=e(1469),m=e(597),p=e(8227),f=e(9519),b=p("isConcatSpreadable"),k=f>=51||!r((function(){var t=[];return t[b]=!1,t.concat()[0]!==t})),v=function(t){if(!i(t))return!1;var n=t[b];return void 0!==n?!!n:u(t)},y=!k||!m("concat");o({target:"Array",proto:!0,arity:1,forced:y},{concat:function(t){var n,e,o,r,u,i=c(this),m=d(i,0),p=0;for(n=-1,o=arguments.length;n<o;n++)if(u=-1===n?i:arguments[n],v(u))for(r=a(u),s(p+r),e=0;e<r;e++,p++)e in u&&l(m,p,u[e]);else s(p+1),l(m,p++,u);return m.length=p,m}})},1699:(t,n,e)=>{var o=e(6518),r=e(9504),u=e(511),i=e(7750),c=e(655),a=e(1436),s=r("".indexOf);o({target:"String",proto:!0,forced:!a("includes")},{includes:function(t){return!!~s(c(i(this)),c(u(t)),arguments.length>1?arguments[1]:void 0)}})}}]);
//# sourceMappingURL=140.c865fedf.js.map