(function(){"use strict";var e={7311:function(e,t,r){var n=r(6848),o=function(){var e=this,t=e._self._c;return t("div",{attrs:{id:"app"}},[t("router-view")],1)},s=[],a={name:"App"},i=a,l=r(1656),c=(0,l.A)(i,o,s,!1,null,null,null),u=c.exports,d=r(6178),m=function(){var e=this,t=e._self._c;return t("div",{staticClass:"outer-container",attrs:{id:"app"}},[t("div",{staticClass:"container"},[t("el-container",[t("el-main",[t("el-form",{attrs:{model:e.form,"label-width":"120px"}},[t("el-form-item",{attrs:{label:"选择食谱"}},[t("el-radio-group",{model:{value:e.form.selectedRecipe,callback:function(t){e.$set(e.form,"selectedRecipe",t)},expression:"form.selectedRecipe"}},[t("el-radio",{attrs:{label:"牛肉汤"}},[e._v("牛肉汤")]),t("el-radio",{attrs:{label:"牛杂汤"}},[e._v("牛杂汤")])],1)],1),t("el-form-item",{attrs:{label:"选择类别"}},[t("el-radio-group",{model:{value:e.form.selectedPrice,callback:function(t){e.$set(e.form,"selectedPrice",t)},expression:"form.selectedPrice"}},e._l(e.prices,(function(r){return t("el-radio",{key:r,attrs:{label:r}},[e._v(e._s(r)+"元")])})),1)],1),t("el-form-item",{attrs:{label:"选择口味"}},[t("el-radio-group",{model:{value:e.form.selectedSpice,callback:function(t){e.$set(e.form,"selectedSpice",t)},expression:"form.selectedSpice"}},e._l(e.spices,(function(r){return t("el-radio",{key:r,attrs:{label:r}},[e._v(e._s(r))])})),1)],1),t("el-form-item",{attrs:{label:"添加香菜"}},[t("el-radio-group",{model:{value:e.form.addCilantro,callback:function(t){e.$set(e.form,"addCilantro",t)},expression:"form.addCilantro"}},[t("el-radio",{attrs:{label:!0}},[e._v("是")]),t("el-radio",{attrs:{label:!1}},[e._v("否")])],1)],1),t("el-form-item",{attrs:{label:"添加葱花"}},[t("el-radio-group",{model:{value:e.form.addOnion,callback:function(t){e.$set(e.form,"addOnion",t)},expression:"form.addOnion"}},[t("el-radio",{attrs:{label:!0}},[e._v("是")]),t("el-radio",{attrs:{label:!1}},[e._v("否")])],1)],1)],1),t("div",{staticClass:"button-container"},[t("el-button",{staticClass:"center-button",attrs:{type:"primary",disabled:"red"===e.server.color},on:{click:e.submitOrder}},[e._v("提交订单")])],1)],1)],1)],1),t("div",{staticClass:"status-container",style:{color:e.server.color}},[t("span",[e._v("设备连接状态: "+e._s(e.server.message))])])])},f=[],p=r(4373),h={name:"App",data(){return{form:{selectedRecipe:"牛肉汤",selectedPrice:20,selectedSpice:"微辣",addCilantro:!0,addOnion:!0},recipes:["牛肉汤","牛杂汤"],prices:[10,15,20],spices:["不辣","微辣","中辣","辣"],orderSubmitted:!1,server:{message:"获取中...",color:"black"}}},methods:{async submitOrder(){if(this.form.selectedRecipe&&this.form.selectedPrice&&this.form.selectedSpice)try{const e=await p.A.post("http://127.0.0.1:8080/orders",this.form,{headers:{"Content-Type":"application/json"}});this.orderSubmitted=!0,this.$message.success("订单提交成功"),console.log(e.data),setTimeout((()=>{location.reload()}),5e3)}catch(e){this.$message.error("订单提交失败"),console.error(e)}else this.$message.error("请完整选择所有选项")},async fetchServerStatus(){try{const e=await p.A.get("http://127.0.0.1:8080/orders/serverStatus");this.server=e.data,console.log("response.data==="+e.data)}catch(e){this.serverMessage="获取失败",this.serverColor="red",console.error(e)}}},mounted(){this.fetchServerStatus(),this.interval=setInterval(this.fetchServerStatus,1e3)},beforeUnmount(){clearInterval(this.interval)}},v=h,b=(0,l.A)(v,m,f,!1,null,null,null),y=b.exports,g=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticClass:"container"},[t("div",{staticClass:"button-columns"},e._l(e.buttonColumns,(function(r,n){return t("div",{key:n,staticClass:"button-column"},e._l(r,(function(r){return t("el-button",{key:r.id,attrs:{type:"primary"},on:{click:function(t){return e.handleButtonClick(r.id)}}},[e._v(" "+e._s(r.name)+" ")])})),1)})),0)]),t("div",{staticClass:"fixed-buttons"},[t("el-button",{staticClass:"reset-button",attrs:{type:"success"},on:{click:e.resetSystem}},[e._v("复位")]),t("el-button",{staticClass:"emergency-button",attrs:{type:"danger"},on:{click:e.emergencyStop}},[e._v("急停")])],1)])},_=[];const C="http://127.0.0.1:8080/buttonAction";var S={name:"ButtonsPage",data(){return{buttons:[{id:1,name:"机器人重置"},{id:2,name:"机器人取碗"},{id:3,name:"机器人出汤"},{id:4,name:"机器人取粉丝"},{id:5,name:"取餐口复位"},{id:6,name:"取餐口出餐"},{id:7,name:"调料机测试（配方500）"},{id:8,name:"抽汤泵（10秒）"},{id:9,name:"转台复位"},{id:10,name:"转台下一工位"},{id:11,name:"碗复位"},{id:12,name:"蒸汽测试（5秒）"},{id:13,name:"蒸汽打开"},{id:14,name:"蒸汽关闭"},{id:15,name:"待办功能"},{id:16,name:"待办功能"}],buttonsPerColumn:8,buttonColumns:[]}},created(){this.buttonColumns=this.chunkArray(this.buttons,this.buttonsPerColumn)},methods:{async handleButtonClick(e){try{const t=await p.A.get(`${C}/${e}`,{headers:{"Content-Type":"application/json"}});this.$message.success(`按钮${e}的操作成功：${t.data}`),console.log(t.data)}catch(t){this.$message.error(`按钮${e}的操作失败`),console.error(t)}},async emergencyStop(){try{const e=await p.A.get(`${C}/emergencyStop`,{headers:{"Content-Type":"application/json"}});this.$message.success("急停操作成功"),console.log(e.data)}catch(e){this.$message.error("急停操作失败"),console.error(e)}},async resetSystem(){try{const e=await p.A.get(`${C}/reset`,{headers:{"Content-Type":"application/json"}});this.$message.success("复位操作成功"),console.log(e.data)}catch(e){this.$message.error("复位操作失败"),console.error(e)}},chunkArray(e,t){return Array.from({length:Math.ceil(e.length/t)},((r,n)=>e.slice(n*t,n*t+t)))}}},k=S,$=(0,l.A)(k,g,_,!1,null,"82dcde30",null),O=$.exports;n["default"].use(d.Ay);var w=new d.Ay({mode:"hash",routes:[{path:"/",name:"OrderPage",component:y},{path:"/buttons",name:"ButtonsPage",component:O}]}),A=r(9143),x=r.n(A);n["default"].use(x()),new n["default"]({router:w,render:e=>e(u)}).$mount("#app")}},t={};function r(n){var o=t[n];if(void 0!==o)return o.exports;var s=t[n]={id:n,loaded:!1,exports:{}};return e[n].call(s.exports,s,s.exports,r),s.loaded=!0,s.exports}r.m=e,function(){r.amdO={}}(),function(){var e=[];r.O=function(t,n,o,s){if(!n){var a=1/0;for(u=0;u<e.length;u++){n=e[u][0],o=e[u][1],s=e[u][2];for(var i=!0,l=0;l<n.length;l++)(!1&s||a>=s)&&Object.keys(r.O).every((function(e){return r.O[e](n[l])}))?n.splice(l--,1):(i=!1,s<a&&(a=s));if(i){e.splice(u--,1);var c=o();void 0!==c&&(t=c)}}return t}s=s||0;for(var u=e.length;u>0&&e[u-1][2]>s;u--)e[u]=e[u-1];e[u]=[n,o,s]}}(),function(){r.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return r.d(t,{a:t}),t}}(),function(){r.d=function(e,t){for(var n in t)r.o(t,n)&&!r.o(e,n)&&Object.defineProperty(e,n,{enumerable:!0,get:t[n]})}}(),function(){r.g=function(){if("object"===typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(e){if("object"===typeof window)return window}}()}(),function(){r.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)}}(),function(){r.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})}}(),function(){r.nmd=function(e){return e.paths=[],e.children||(e.children=[]),e}}(),function(){var e={524:0};r.O.j=function(t){return 0===e[t]};var t=function(t,n){var o,s,a=n[0],i=n[1],l=n[2],c=0;if(a.some((function(t){return 0!==e[t]}))){for(o in i)r.o(i,o)&&(r.m[o]=i[o]);if(l)var u=l(r)}for(t&&t(n);c<a.length;c++)s=a[c],r.o(e,s)&&e[s]&&e[s][0](),e[s]=0;return r.O(u)},n=self["webpackChunkfrontend"]=self["webpackChunkfrontend"]||[];n.forEach(t.bind(null,0)),n.push=t.bind(null,n.push.bind(n))}();var n=r.O(void 0,[504],(function(){return r(7311)}));n=r.O(n)})();
//# sourceMappingURL=app.f3f51378.js.map