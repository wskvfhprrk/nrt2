(function(){"use strict";var e={7370:function(e,t,r){var n=r(6848),o=function(){var e=this,t=e._self._c;return t("div",{attrs:{id:"app"}},[t("router-view")],1)},a=[],i={name:"App"},l=i,s=r(1656),c=(0,s.A)(l,o,a,!1,null,null,null),u=c.exports,d=r(6178),f=function(){var e=this,t=e._self._c;return t("div",{staticClass:"outer-container",attrs:{id:"app"}},[t("div",{staticClass:"container"},[t("el-container",[t("el-main",[t("el-form",{attrs:{model:e.form,"label-width":"120px"}},[t("el-form-item",{attrs:{label:"选择食谱"}},[t("el-radio-group",{model:{value:e.form.selectedRecipe,callback:function(t){e.$set(e.form,"selectedRecipe",t)},expression:"form.selectedRecipe"}},[t("el-radio",{attrs:{label:"牛肉汤"}},[e._v("牛肉汤")]),t("el-radio",{attrs:{label:"牛杂汤"}},[e._v("牛杂汤")])],1)],1),t("el-form-item",{attrs:{label:"选择类别"}},[t("el-radio-group",{model:{value:e.form.selectedPrice,callback:function(t){e.$set(e.form,"selectedPrice",t)},expression:"form.selectedPrice"}},e._l(e.prices,(function(r){return t("el-radio",{key:r,attrs:{label:r}},[e._v(e._s(r)+"元")])})),1)],1),t("el-form-item",{attrs:{label:"选择口味"}},[t("el-radio-group",{model:{value:e.form.selectedSpice,callback:function(t){e.$set(e.form,"selectedSpice",t)},expression:"form.selectedSpice"}},e._l(e.spices,(function(r){return t("el-radio",{key:r,attrs:{label:r}},[e._v(e._s(r))])})),1)],1),t("el-form-item",{attrs:{label:"添加香菜"}},[t("el-radio-group",{model:{value:e.form.addCilantro,callback:function(t){e.$set(e.form,"addCilantro",t)},expression:"form.addCilantro"}},[t("el-radio",{attrs:{label:!0}},[e._v("是")]),t("el-radio",{attrs:{label:!1}},[e._v("否")])],1)],1),t("el-form-item",{attrs:{label:"添加葱花"}},[t("el-radio-group",{model:{value:e.form.addOnion,callback:function(t){e.$set(e.form,"addOnion",t)},expression:"form.addOnion"}},[t("el-radio",{attrs:{label:!0}},[e._v("是")]),t("el-radio",{attrs:{label:!1}},[e._v("否")])],1)],1)],1),t("div",{staticClass:"button-container"},[t("el-button",{staticClass:"center-button",attrs:{type:"primary"},on:{click:e.submitOrder}},[e._v("提交订单")])],1)],1)],1)],1)])},p=[],m=r(4373),b={name:"App",data(){return{form:{selectedRecipe:"牛肉汤",selectedPrice:20,selectedSpice:"微辣",addCilantro:!0,addOnion:!0},recipes:["牛肉汤","牛杂汤"],prices:[10,15,20],spices:["不辣","微辣","中辣","辣"],orderSubmitted:!1}},methods:{async submitOrder(){if(this.form.selectedRecipe&&this.form.selectedPrice&&this.form.selectedSpice)try{const e=await m.A.post("http://127.0.0.1:8080/orders",this.form,{headers:{"Content-Type":"application/json"}});this.orderSubmitted=!0,this.$message.success("订单提交成功"),console.log(e.data),setTimeout((()=>{location.reload()}),5e3)}catch(e){this.$message.error("订单提交失败"),console.error(e)}else this.$message.error("请完整选择所有选项")}}},v=b,h=(0,s.A)(v,f,p,!1,null,null,null),y=h.exports,g=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticClass:"button-container"},e._l(e.buttons,(function(r){return t("el-button",{key:r.id,attrs:{type:"primary"},on:{click:function(t){return e.handleButtonClick(r.id)}}},[e._v(" 按钮"+e._s(r.id)+" ")])})),1)])},_=[],O={name:"ButtonsPage",data(){return{buttons:Array.from({length:10},((e,t)=>({id:t+1})))}},methods:{async handleButtonClick(e){try{const t=await m.A.get(`http://127.0.0.1:8080/buttonAction/${e}`,{headers:{"Content-Type":"application/json"}});this.$message.success(`按钮${e}的操作成功`),console.log(t.data)}catch(t){this.$message.error(`按钮${e}的操作失败`),console.error(t)}}}},k=O,C=(0,s.A)(k,g,_,!1,null,"8505deaa",null),w=C.exports;n["default"].use(d.Ay);var x=new d.Ay({mode:"hash",routes:[{path:"/",name:"OrderPage",component:y},{path:"/buttons",name:"ButtonsPage",component:w}]}),$=r(9143),P=r.n($);n["default"].use(P()),new n["default"]({router:x,render:e=>e(u)}).$mount("#app")}},t={};function r(n){var o=t[n];if(void 0!==o)return o.exports;var a=t[n]={id:n,loaded:!1,exports:{}};return e[n].call(a.exports,a,a.exports,r),a.loaded=!0,a.exports}r.m=e,function(){r.amdO={}}(),function(){var e=[];r.O=function(t,n,o,a){if(!n){var i=1/0;for(u=0;u<e.length;u++){n=e[u][0],o=e[u][1],a=e[u][2];for(var l=!0,s=0;s<n.length;s++)(!1&a||i>=a)&&Object.keys(r.O).every((function(e){return r.O[e](n[s])}))?n.splice(s--,1):(l=!1,a<i&&(i=a));if(l){e.splice(u--,1);var c=o();void 0!==c&&(t=c)}}return t}a=a||0;for(var u=e.length;u>0&&e[u-1][2]>a;u--)e[u]=e[u-1];e[u]=[n,o,a]}}(),function(){r.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return r.d(t,{a:t}),t}}(),function(){r.d=function(e,t){for(var n in t)r.o(t,n)&&!r.o(e,n)&&Object.defineProperty(e,n,{enumerable:!0,get:t[n]})}}(),function(){r.g=function(){if("object"===typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(e){if("object"===typeof window)return window}}()}(),function(){r.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)}}(),function(){r.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})}}(),function(){r.nmd=function(e){return e.paths=[],e.children||(e.children=[]),e}}(),function(){var e={524:0};r.O.j=function(t){return 0===e[t]};var t=function(t,n){var o,a,i=n[0],l=n[1],s=n[2],c=0;if(i.some((function(t){return 0!==e[t]}))){for(o in l)r.o(l,o)&&(r.m[o]=l[o]);if(s)var u=s(r)}for(t&&t(n);c<i.length;c++)a=i[c],r.o(e,a)&&e[a]&&e[a][0](),e[a]=0;return r.O(u)},n=self["webpackChunkfrontend"]=self["webpackChunkfrontend"]||[];n.forEach(t.bind(null,0)),n.push=t.bind(null,n.push.bind(n))}();var n=r.O(void 0,[504],(function(){return r(7370)}));n=r.O(n)})();
//# sourceMappingURL=app.a6c918e1.js.map