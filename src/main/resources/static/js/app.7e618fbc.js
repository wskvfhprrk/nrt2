(()=>{"use strict";var e={6445:(e,t,n)=>{n(3792),n(3362),n(9085),n(9391);var r=n(3751),o=n(641),a={id:"app"};function u(e,t,n,r,u,i){var c=(0,o.g2)("router-view");return(0,o.uX)(),(0,o.CE)("div",a,[(0,o.bF)(c)])}const i={name:"App"};var c=n(6262);const s=(0,c.A)(i,[["render",u],["__scopeId","data-v-987163be"]]),l=s;n(4423),n(2010),n(6099),n(7764),n(2953);var d=n(5220);function f(e,t,n,r,a,u){var i=(0,o.g2)("Head"),c=(0,o.g2)("el-header"),s=(0,o.g2)("Menu"),l=(0,o.g2)("el-aside"),d=(0,o.g2)("router-view"),f=(0,o.g2)("el-main"),p=(0,o.g2)("el-container");return(0,o.uX)(),(0,o.Wv)(p,null,{default:(0,o.k6)((function(){return[u.isShow?(0,o.Q3)("",!0):((0,o.uX)(),(0,o.Wv)(c,{key:0},{default:(0,o.k6)((function(){return[(0,o.bF)(i)]})),_:1})),(0,o.bF)(p,null,{default:(0,o.k6)((function(){return[u.isShow?(0,o.Q3)("",!0):((0,o.uX)(),(0,o.Wv)(l,{key:0,width:"200px"},{default:(0,o.k6)((function(){return[(0,o.bF)(s)]})),_:1})),(0,o.bF)(f,null,{default:(0,o.k6)((function(){return[(0,o.bF)(d)]})),_:1})]})),_:1})]})),_:1})}const p=n.p+"img/logo.1361b45b.png";var m=function(e){return(0,o.Qi)("data-v-80e73974"),e=e(),(0,o.jt)(),e},h={class:"title"},g=m((function(){return(0,o.Lk)("img",{src:p},null,-1)})),v=m((function(){return(0,o.Lk)("span",{class:"text"},"牛羊肉汤自动售卖机后台管理",-1)}));function b(e,t,n,r,a,u){var i=(0,o.g2)("el-avatar");return(0,o.uX)(),(0,o.CE)("div",h,[(0,o.bF)(i,{size:50,src:"https://empty",onError:e.errorHandler},{default:(0,o.k6)((function(){return[g]})),_:1},8,["onError"]),v])}const k={name:"Head"},x=(0,c.A)(k,[["render",b],["__scopeId","data-v-80e73974"]]),y=x;function S(e,t,n,r,a,u){var i=(0,o.g2)("el-menu-item"),c=(0,o.g2)("el-menu-item-group"),s=(0,o.g2)("el-menu");return(0,o.uX)(),(0,o.CE)("div",null,[(0,o.bF)(s,{"default-active":a.activeIndex,class:"custom-menu",onSelect:u.handleSelect,"background-color":a.menuBackgroundColor,"text-color":a.menuTextColor,"active-text-color":a.menuActiveTextColor},{default:(0,o.k6)((function(){return[(0,o.bF)(c,{title:"返回点餐"},{default:(0,o.k6)((function(){return[(0,o.bF)(i,{index:"1"},{default:(0,o.k6)((function(){return[(0,o.eW)(" 点餐 ")]})),_:1})]})),_:1}),(0,o.bF)(c,{title:"设置页面"},{default:(0,o.k6)((function(){return[(0,o.bF)(i,{index:"2"},{default:(0,o.k6)((function(){return[(0,o.eW)(" 销售统计 ")]})),_:1}),(0,o.bF)(i,{index:"3"},{default:(0,o.k6)((function(){return[(0,o.eW)(" 清洗操作 ")]})),_:1}),(0,o.bF)(i,{index:"4"},{default:(0,o.k6)((function(){return[(0,o.eW)(" 自动运行 ")]})),_:1}),(0,o.bF)(i,{index:"5"},{default:(0,o.k6)((function(){return[(0,o.eW)(" 手动操作 ")]})),_:1}),(0,o.bF)(i,{index:"6"},{default:(0,o.k6)((function(){return[(0,o.eW)(" 报警设置 ")]})),_:1}),(0,o.bF)(i,{index:"7"},{default:(0,o.k6)((function(){return[(0,o.eW)(" 份量设置 ")]})),_:1}),(0,o.bF)(i,{index:"8"},{default:(0,o.k6)((function(){return[(0,o.eW)(" 点餐设置 ")]})),_:1}),(0,o.bF)(i,{index:"9"},{default:(0,o.k6)((function(){return[(0,o.eW)(" 价格设置 ")]})),_:1}),(0,o.bF)(i,{index:"10"},{default:(0,o.k6)((function(){return[(0,o.eW)(" 机器账户设置 ")]})),_:1})]})),_:1})]})),_:1},8,["default-active","onSelect","background-color","text-color","active-text-color"])])}n(4114),n(2762);const w={name:"Menu",data:function(){return{activeIndex:this.getActiveIndex(),menuBackgroundColor:"#eeeeee",menuTextColor:"#333333",menuActiveTextColor:"#ffffff"}},mounted:function(){var e=getComputedStyle(document.documentElement);this.menuBackgroundColor=e.getPropertyValue("--gray").trim(),this.menuTextColor=e.getPropertyValue("--gray").trim(),this.menuActiveTextColor=e.getPropertyValue("--silver").trim()},methods:{handleSelect:function(e){switch(this.activeIndex=e,e){case"1":this.$router.push("/");break;case"2":this.$router.push("/salesStatistics");break;case"3":this.$router.push("/cleaningOperation");break;case"4":this.$router.push("/automaticOperation");break;case"5":this.$router.push("/manualOperation");break;case"6":this.$router.push("/alarmSettings");break;case"7":this.$router.push("/portionSettings");break;case"8":this.$router.push("/orderingSettings");break;case"9":this.$router.push("/priceSettings");break;case"10":this.$router.push("/setAccount");break;default:this.$router.push("/")}},getActiveIndex:function(){var e=this.$route.path;switch(e){case"/salesStatistics":return"2";case"/cleaningOperation":return"3";case"/automaticOperation":return"4";case"/manualOperation":return"5";case"/alarmSettings":return"6";case"/portionSettings":return"7";case"/orderingSettings":return"8";case"/priceSettings":return"9";case"/setAccount":return"10";default:return"1"}}},watch:{$route:function(e){this.activeIndex=this.getActiveIndex()}}},_=(0,c.A)(w,[["render",S],["__scopeId","data-v-2d072e70"]]),A=_,O={name:"MainLayout",components:{Head:y,Menu:A},computed:{isShow:function(){return"/"===this.$route.path}}},C=(0,c.A)(O,[["render",f],["__scopeId","data-v-ef3f8400"]]),F=C;var E=n(33),L=function(e){return(0,o.Qi)("data-v-58e39d72"),e=e(),(0,o.jt)(),e},T={class:"login-page"},$=L((function(){return(0,o.Lk)("div",{class:"background"},null,-1)})),I={class:"login-box"},P=L((function(){return(0,o.Lk)("h2",null,"售卖机后台管理",-1)})),W={class:"form-group"},j={class:"form-group"},N=L((function(){return(0,o.Lk)("button",{type:"submit",class:"login-button"},"登录",-1)}));function B(e,t,n,a,u,i){var c=(0,o.g2)("el-alert");return(0,o.uX)(),(0,o.CE)("div",T,[$,(0,o.Lk)("div",I,[P,(0,o.Lk)("form",{onSubmit:t[2]||(t[2]=(0,r.D$)((function(){return i.submitForm&&i.submitForm.apply(i,arguments)}),["prevent"]))},[(0,o.Lk)("div",W,[(0,o.bo)((0,o.Lk)("input",{type:"text","onUpdate:modelValue":t[0]||(t[0]=function(e){return u.username=e}),placeholder:"请输入账号",required:""},null,512),[[r.Jo,u.username]])]),(0,o.Lk)("div",j,[(0,o.bo)((0,o.Lk)("input",{type:"password","onUpdate:modelValue":t[1]||(t[1]=function(e){return u.password=e}),placeholder:"请输入密码",required:""},null,512),[[r.Jo,u.password]])]),N],32),u.showAlert?((0,o.uX)(),(0,o.Wv)(c,{key:0,type:"error",center:"","show-icon":""},{default:(0,o.k6)((function(){return[(0,o.eW)((0,E.v_)(u.tex),1)]})),_:1})):(0,o.Q3)("",!0)])])}var M=n(459),X=n(388),H=n(2505),Q=n.n(H),V="http://127.0.0.1:8080";const q={data:function(){return{username:"",password:"",showAlert:!1,tex:""}},methods:{submitForm:function(){var e=this;return(0,X.A)((0,M.A)().mark((function t(){var n,r,o;return(0,M.A)().wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,n="".concat(V,"/login"),t.next=4,Q().post(n,{username:e.username,password:e.password});case 4:if(r=t.sent,200!==r.data.code){t.next=11;break}o=r.data.data.token,localStorage.setItem("userToken",o),e.$router.push({name:"SalesStatistics"}),t.next=14;break;case 11:return e.tex=r.data.message,e.showAlert=!0,t.abrupt("return");case 14:t.next=20;break;case 16:t.prev=16,t.t0=t["catch"](0),e.tex="登录失败，请检查账号或密码",e.showAlert=!0;case 20:case"end":return t.stop()}}),t,null,[[0,16]])})))()}}},D=(0,c.A)(q,[["render",B],["__scopeId","data-v-58e39d72"]]),U=D;var J=function(){return n.e(658).then(n.bind(n,1231))},z=function(){return n.e(983).then(n.bind(n,6983))},K=function(){return n.e(334).then(n.bind(n,8334))},G=function(){return n.e(403).then(n.bind(n,5403))},R=function(){return n.e(339).then(n.bind(n,6339))},Y=function(){return n.e(243).then(n.bind(n,5243))},Z=function(){return n.e(871).then(n.bind(n,4871))},ee=function(){return n.e(61).then(n.bind(n,5061))},te=function(){return n.e(160).then(n.bind(n,3160))},ne=function(){return n.e(104).then(n.bind(n,4104))},re=function(){return n.e(680).then(n.bind(n,5299))},oe=[{path:"/login",name:"Login",component:U},{path:"/",component:F,children:[{path:"",name:"OrderPage",component:J},{path:"buttons",name:"ButtonsPage",component:z},{path:"salesStatistics",name:"SalesStatistics",component:K},{path:"cleaningOperation",name:"CleaningOperation",component:G},{path:"automaticOperation",name:"AutomaticOperation",component:R},{path:"manualOperation",name:"ManualOperation",component:Y},{path:"alarmSettings",name:"AlarmSettings",component:Z},{path:"portionSettings",name:"PortionSettings",component:ee},{path:"orderingSettings",name:"OrderingSettings",component:te},{path:"priceSettings",name:"PriceSettings",component:ne},{path:"setAccount",name:"SetAccount",component:re}]}],ae=function(){return!!localStorage.getItem("userToken")},ue=(0,d.aE)({history:(0,d.LA)(),routes:oe});ue.beforeEach((function(e,t,n){var r=["OrderPage","Login"],o=!r.includes(e.name),a=ae();if(o&&!a)return n("/login");n()}));const ie=ue;var ce=n(5484);n(4188);(0,r.Ef)(l).use(ie).use(ce.A).mount("#app")}},t={};function n(r){var o=t[r];if(void 0!==o)return o.exports;var a=t[r]={exports:{}};return e[r].call(a.exports,a,a.exports,n),a.exports}n.m=e,(()=>{var e=[];n.O=(t,r,o,a)=>{if(!r){var u=1/0;for(l=0;l<e.length;l++){for(var[r,o,a]=e[l],i=!0,c=0;c<r.length;c++)(!1&a||u>=a)&&Object.keys(n.O).every((e=>n.O[e](r[c])))?r.splice(c--,1):(i=!1,a<u&&(u=a));if(i){e.splice(l--,1);var s=o();void 0!==s&&(t=s)}}return t}a=a||0;for(var l=e.length;l>0&&e[l-1][2]>a;l--)e[l]=e[l-1];e[l]=[r,o,a]}})(),(()=>{n.n=e=>{var t=e&&e.__esModule?()=>e["default"]:()=>e;return n.d(t,{a:t}),t}})(),(()=>{n.d=(e,t)=>{for(var r in t)n.o(t,r)&&!n.o(e,r)&&Object.defineProperty(e,r,{enumerable:!0,get:t[r]})}})(),(()=>{n.f={},n.e=e=>Promise.all(Object.keys(n.f).reduce(((t,r)=>(n.f[r](e,t),t)),[]))})(),(()=>{n.u=e=>"js/"+e+"."+{61:"897906ec",104:"62fac8a9",160:"a2eb2739",243:"f8df6911",334:"fe5576b7",339:"605534b7",403:"2717c25b",658:"f78f512c",680:"8fe902ca",871:"3320c522",983:"d8a31da5"}[e]+".js"})(),(()=>{n.miniCssF=e=>"css/"+e+"."+{243:"1dc50f59",658:"215049e0",983:"7efa72a2"}[e]+".css"})(),(()=>{n.g=function(){if("object"===typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(e){if("object"===typeof window)return window}}()})(),(()=>{n.o=(e,t)=>Object.prototype.hasOwnProperty.call(e,t)})(),(()=>{var e={},t="vue:";n.l=(r,o,a,u)=>{if(e[r])e[r].push(o);else{var i,c;if(void 0!==a)for(var s=document.getElementsByTagName("script"),l=0;l<s.length;l++){var d=s[l];if(d.getAttribute("src")==r||d.getAttribute("data-webpack")==t+a){i=d;break}}i||(c=!0,i=document.createElement("script"),i.charset="utf-8",i.timeout=120,n.nc&&i.setAttribute("nonce",n.nc),i.setAttribute("data-webpack",t+a),i.src=r),e[r]=[o];var f=(t,n)=>{i.onerror=i.onload=null,clearTimeout(p);var o=e[r];if(delete e[r],i.parentNode&&i.parentNode.removeChild(i),o&&o.forEach((e=>e(n))),t)return t(n)},p=setTimeout(f.bind(null,void 0,{type:"timeout",target:i}),12e4);i.onerror=f.bind(null,i.onerror),i.onload=f.bind(null,i.onload),c&&document.head.appendChild(i)}}})(),(()=>{n.r=e=>{"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})}})(),(()=>{n.p="/"})(),(()=>{if("undefined"!==typeof document){var e=(e,t,r,o,a)=>{var u=document.createElement("link");u.rel="stylesheet",u.type="text/css",n.nc&&(u.nonce=n.nc);var i=n=>{if(u.onerror=u.onload=null,"load"===n.type)o();else{var r=n&&n.type,i=n&&n.target&&n.target.href||t,c=new Error("Loading CSS chunk "+e+" failed.\n("+r+": "+i+")");c.name="ChunkLoadError",c.code="CSS_CHUNK_LOAD_FAILED",c.type=r,c.request=i,u.parentNode&&u.parentNode.removeChild(u),a(c)}};return u.onerror=u.onload=i,u.href=t,r?r.parentNode.insertBefore(u,r.nextSibling):document.head.appendChild(u),u},t=(e,t)=>{for(var n=document.getElementsByTagName("link"),r=0;r<n.length;r++){var o=n[r],a=o.getAttribute("data-href")||o.getAttribute("href");if("stylesheet"===o.rel&&(a===e||a===t))return o}var u=document.getElementsByTagName("style");for(r=0;r<u.length;r++){o=u[r],a=o.getAttribute("data-href");if(a===e||a===t)return o}},r=r=>new Promise(((o,a)=>{var u=n.miniCssF(r),i=n.p+u;if(t(u,i))return o();e(r,i,null,o,a)})),o={524:0};n.f.miniCss=(e,t)=>{var n={243:1,658:1,983:1};o[e]?t.push(o[e]):0!==o[e]&&n[e]&&t.push(o[e]=r(e).then((()=>{o[e]=0}),(t=>{throw delete o[e],t})))}}})(),(()=>{var e={524:0};n.f.j=(t,r)=>{var o=n.o(e,t)?e[t]:void 0;if(0!==o)if(o)r.push(o[2]);else{var a=new Promise(((n,r)=>o=e[t]=[n,r]));r.push(o[2]=a);var u=n.p+n.u(t),i=new Error,c=r=>{if(n.o(e,t)&&(o=e[t],0!==o&&(e[t]=void 0),o)){var a=r&&("load"===r.type?"missing":r.type),u=r&&r.target&&r.target.src;i.message="Loading chunk "+t+" failed.\n("+a+": "+u+")",i.name="ChunkLoadError",i.type=a,i.request=u,o[1](i)}};n.l(u,c,"chunk-"+t,t)}},n.O.j=t=>0===e[t];var t=(t,r)=>{var o,a,[u,i,c]=r,s=0;if(u.some((t=>0!==e[t]))){for(o in i)n.o(i,o)&&(n.m[o]=i[o]);if(c)var l=c(n)}for(t&&t(r);s<u.length;s++)a=u[s],n.o(e,a)&&e[a]&&e[a][0](),e[a]=0;return n.O(l)},r=self["webpackChunkvue"]=self["webpackChunkvue"]||[];r.forEach(t.bind(null,0)),r.push=t.bind(null,r.push.bind(r))})();var r=n.O(void 0,[504],(()=>n(6445)));r=n.O(r)})();
//# sourceMappingURL=app.7e618fbc.js.map