(function(t){function e(e){for(var s,r,a=e[0],l=e[1],c=e[2],h=0,d=[];h<a.length;h++)r=a[h],Object.prototype.hasOwnProperty.call(o,r)&&o[r]&&d.push(o[r][0]),o[r]=0;for(s in l)Object.prototype.hasOwnProperty.call(l,s)&&(t[s]=l[s]);u&&u(e);while(d.length)d.shift()();return i.push.apply(i,c||[]),n()}function n(){for(var t,e=0;e<i.length;e++){for(var n=i[e],s=!0,a=1;a<n.length;a++){var l=n[a];0!==o[l]&&(s=!1)}s&&(i.splice(e--,1),t=r(r.s=n[0]))}return t}var s={},o={app:0},i=[];function r(e){if(s[e])return s[e].exports;var n=s[e]={i:e,l:!1,exports:{}};return t[e].call(n.exports,n,n.exports,r),n.l=!0,n.exports}r.m=t,r.c=s,r.d=function(t,e,n){r.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:n})},r.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},r.t=function(t,e){if(1&e&&(t=r(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var n=Object.create(null);if(r.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var s in t)r.d(n,s,function(e){return t[e]}.bind(null,s));return n},r.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return r.d(e,"a",e),e},r.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},r.p="";var a=window["webpackJsonp"]=window["webpackJsonp"]||[],l=a.push.bind(a);a.push=e,a=a.slice();for(var c=0;c<a.length;c++)e(a[c]);var u=l;i.push([0,"chunk-vendors"]),n()})({0:function(t,e,n){t.exports=n("56d7")},"0a0a":function(t,e,n){},"0c06":function(t,e,n){"use strict";n("8533")},"3eca":function(t,e,n){"use strict";n("d7b7")},5502:function(t,e,n){"use strict";n("0a0a")},"56d7":function(t,e,n){"use strict";n.r(e);n("e260"),n("e6cf"),n("cca6"),n("a79d");var s=n("2b0e"),o=(n("0fb7"),n("450d"),n("f529")),i=n.n(o),r=(n("0c67"),n("299c")),a=n.n(r),l=(n("e3ea"),n("7bc3")),c=n.n(l),u=(n("e612"),n("dd87")),h=n.n(u),d=(n("075a"),n("72aa")),p=n.n(d),m=(n("960d"),n("defb")),f=n.n(m),b=(n("bd49"),n("18ff")),g=n.n(b),v=(n("cb70"),n("b370")),w=n.n(v),y=(n("f225"),n("89a9")),k=n.n(y),T=(n("de31"),n("c69e")),S=n.n(T),$=(n("a673"),n("7b31")),x=n.n($),_=(n("adec"),n("3d2d")),L=n.n(_),I=(n("5466"),n("ecdf")),C=n.n(I),O=(n("38a0"),n("ad41")),P=n.n(O),E=(n("ae26"),n("845f")),F=n.n(E),j=(n("1951"),n("eedf")),z=n.n(j),M=(n("eca7"),n("3787")),N=n.n(M),D=(n("425f"),n("4105")),R=n.n(D),V=(n("f4f9"),n("c2cc")),W=n.n(V),H=(n("7a0f"),n("0f6c")),A=n.n(H),q=(n("a7cc"),n("df33")),K=n.n(q),U=(n("10cb"),n("f3ad")),B=n.n(U),G={install:function(t){t.use(B.a),t.use(K.a),t.use(A.a),t.use(W.a),t.use(R.a),t.use(N.a),t.use(z.a),t.use(F.a),t.use(P.a),t.use(C.a),t.use(L.a),t.use(x.a),t.use(S.a),t.use(k.a),t.use(w.a),t.use(g.a),t.use(f.a),t.use(p.a),t.use(h.a),t.use(c.a),t.use(a.a),t.prototype.$message=i.a}},J=G,X=(n("0fae"),n("b20f"),function(){var t=this,e=this,n=e._self._c;return n("div",{attrs:{id:"app"}},[n("el-container",[n("el-header",{staticStyle:{height:"auto"}},[n("vheader",{on:{"ssh-select":function(){t.$refs.tabs.openTerm()},"ssh-logout":function(){t.$refs.tabs.closeTabs("all")}}})],1),n("el-container",[n("el-main",{staticStyle:{padding:"0"}},[n("tabs",{ref:"tabs"})],1)],1)],1)],1)}),Y=[],Q=function(){var t=this,e=t._self._c;return e("div",[e("el-row",[e("el-col",[e("el-form",{staticStyle:{"padding-top":"10px"},attrs:{inline:!0,model:t.sshInfo,rules:t.checkRules}},[e("el-form-item",{attrs:{size:"small",prop:"host"}},[e("template",{slot:"label"},[e("el-tooltip",{attrs:{effect:"dark",placement:"left"}},[e("div",{attrs:{slot:"content"},slot:"content"},[e("p",[t._v("Switch Language")])]),e("span",{on:{click:function(e){return t.handleSetLanguage()}}},[t._v("Host")])])],1),e("el-input",{attrs:{placeholder:t.$t("hostTip")},nativeOn:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.$emit("ssh-select")}},model:{value:t.sshInfo.host,callback:function(e){t.$set(t.sshInfo,"host",e)},expression:"sshInfo.host"}})],2),e("el-form-item",{attrs:{label:"Port",size:"small",prop:"port"}},[e("el-input",{staticStyle:{width:"100px"},attrs:{placeholder:t.$t("portTip")},nativeOn:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.$emit("ssh-select")}},model:{value:t.sshInfo.port,callback:function(e){t.$set(t.sshInfo,"port",e)},expression:"sshInfo.port"}})],1),e("el-form-item",{attrs:{label:"Username",size:"small",prop:"username"}},[e("el-input",{staticStyle:{width:"110px"},attrs:{placeholder:t.$t("nameTip")},nativeOn:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.$emit("ssh-select")}},model:{value:t.sshInfo.username,callback:function(e){t.$set(t.sshInfo,"username",e)},expression:"sshInfo.username"}})],1),e("el-form-item",{attrs:{size:"small",prop:"password"}},[e("template",{slot:"label"},[e("el-tooltip",{attrs:{effect:"dark",placement:"left"}},[e("div",{attrs:{slot:"content"},slot:"content"},[e("p",[t._v(t._s("Switch to ".concat(this.privateKey?"Password":"PrivateKey"," login")))])]),e("span",{on:{click:function(e){0===t.sshInfo.logintype?t.sshInfo.logintype=1:t.sshInfo.logintype=0}}},[t._v(t._s(t.privateKey?"PrivateKey":"Password"))])])],1),e("el-input",{attrs:{placeholder:t.$t("inputTip")+"".concat(this.privateKey?t.$t("privateKey"):t.$t("password")),"show-password":""},nativeOn:{click:function(e){t.textareaVisible=t.privateKey},keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.$emit("ssh-select")}},model:{value:t.sshInfo.password,callback:function(e){t.$set(t.sshInfo,"password",e)},expression:"sshInfo.password"}})],2),e("el-dialog",{attrs:{title:t.$t("privateKey"),visible:t.textareaVisible,"close-on-click-modal":!1},on:{"update:visible":function(e){t.textareaVisible=e}}},[e("el-input",{attrs:{rows:8,type:"textarea",placeholder:t.$t("keyTip")},model:{value:t.sshInfo.password,callback:function(e){t.$set(t.sshInfo,"password",e)},expression:"sshInfo.password"}}),e("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("input",{ref:"pkFile",staticStyle:{position:"absolute",clip:"rect(0 0 0 0)"},attrs:{type:"file"},on:{change:t.handleChangePKFile}}),e("el-button",{attrs:{type:"primary",plain:""},on:{click:function(e){return t.$refs.pkFile.click()}}},[t._v(t._s(t.$t("SelectFile"))+" ")]),e("el-button",{on:{click:function(e){t.sshInfo.password=""}}},[t._v(t._s(t.$t("Clear")))]),e("el-button",{attrs:{type:"primary"},on:{click:function(e){t.textareaVisible=!1,t.$emit("ssh-select")}}},[t._v(t._s(t.$t("Connect"))+" ")])],1)],1),e("el-form-item",{attrs:{size:"small"}},[e("el-button",{attrs:{type:"primary",plain:""},on:{click:function(e){return t.$emit("ssh-select")}}},[t._v(t._s(t.$t("Connect")))])],1),e("el-form-item",{attrs:{size:"small"}},[e("file-list")],1),e("el-form-item",{attrs:{size:"small"}},[e("el-dropdown",{on:{command:t.handleCommand}},[e("el-button",{attrs:{type:"primary"}},[t._v(" "+t._s(t.$t("History"))+" ")]),e("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},t._l(t.sshList,(function(n){return e("el-dropdown-item",{key:n.host,staticStyle:{padding:"0 5px 0 10px"},attrs:{command:n}},[t._v(" "+t._s(n.host)+" "),e("i",{staticClass:"el-icon-close",on:{click:function(e){return t.cleanHistory(n)}}})])})),1)],1)],1),e("el-form-item",{attrs:{size:"small"}},[e("el-button",{directives:[{name:"show",rawName:"v-show",value:t.showLogin,expression:"showLogin"}],attrs:{type:"primary",plain:""},on:{click:function(e){t.foreShowLogin=!0}}},[t._v(t._s(t.$t("login"))+" ")])],1),e("el-form-item",{attrs:{size:"small"}},[e("el-button",{directives:[{name:"show",rawName:"v-show",value:t.showLogout,expression:"showLogout"}],attrs:{type:"primary",plain:""},on:{click:function(e){return t.handleLogout()}}},[t._v(t._s(t.$t("logout"))+" ")])],1)],1)],1)],1),e("el-dialog",{attrs:{title:t.$t("loginFormTitle"),visible:t.showLogin,width:"30%"},on:{"update:visible":function(e){t.showLogin=e}}},[e("el-form",{ref:"loginForm",attrs:{model:t.login.form,rules:t.login.checkRules}},[e("el-form-item",{attrs:{label:"用户名",prop:"username"}},[e("el-input",{attrs:{placeholder:"请输入用户名"},model:{value:t.login.form.username,callback:function(e){t.$set(t.login.form,"username",e)},expression:"login.form.username"}})],1),e("el-form-item",{attrs:{label:"密码",prop:"password"}},[e("el-input",{attrs:{placeholder:"请输入密码",type:"password"},model:{value:t.login.form.password,callback:function(e){t.$set(t.login.form,"password",e)},expression:"login.form.password"}})],1)],1),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{attrs:{type:"primary",plain:""},on:{click:t.handleLogin}},[t._v(t._s(t.$t("login")))])],1)],1)],1)},Z=[],tt=n("5530"),et=(n("a9e3"),n("d3b7"),n("159b"),n("a434"),n("313d"),n("0eb6"),n("b7ef"),n("8bd4"),n("e9c4"),n("ac1f"),n("841c"),n("4d63"),n("c607"),n("2c3e"),n("25f0"),n("b64b"),n("81b2"),n("99af"),n("c7eb")),nt=n("1da1"),st=n("cee4"),ot=n("2f62"),it=n("a925"),rt=n("4897"),at=n.n(rt),lt=n("b2d6"),ct=n.n(lt),ut=n("f0d9"),ht=n.n(ut),dt={hostTip:"please input remote host",portTip:"input port",nameTip:"please input username",inputTip:"please input ",keyTip:"please paste privateKey content",wsClose:"websocket connection disconnected!",notCloseWindows:"please do not close windows",unlockClose:"please unlock to close tab",clickSelectFile:"click to select upload file",clickSelectFolder:"click to select upload folder",uploadFinish:" upload finish",login:"login",logout:"logout",loginFormTitle:"please login",PermissionsString:"permissions",CreateTime:"add time",GroupName:"group",OwnerName:"owner"},pt={OK:"确定",Cancel:"取消",History:"历史记录",Lock:"锁定",Unlock:"解锁",Size:"大小",PermissionsString:"权限",GroupName:"所属组",OwnerName:"所有者",CreateTime:"创建时间",Name:"名字",Copy:"复制",Upload:"上传",Rename:"重命名",FullScreen:"全屏",Close:"关闭",CloseLeft:"关闭左边",CloseRight:"关闭右边",CloseOther:"关闭其他",CloseAll:"关闭所有",FileBrowser:"文件管理",ModifiedTime:"修改时间",Clear:"清空",Connect:"连接",password:"密码",privateKey:"密钥",hostTip:"请输入远程host地址",portTip:"请输入端口",nameTip:"请输入用户名",inputTip:"请输入",keyTip:"请粘贴私钥内容",SelectFile:"选择文件",wsClose:"websocket连接已断开!",uploadPath:"当前上传目录",uploadFinish:"上传完成",to:"到",uploading:"正在上传",notCloseWindows:"请勿关闭窗口",unlockClose:"请解锁后再来关闭",clickSelectFile:"点击选择文件",clickSelectFolder:"点击选择文件夹",uploadFile:"上传文件",uploadFolder:"上传文件夹",login:"登录",logout:"注销",loginFormTitle:"请输入账号登录"};s["default"].use(it["a"]);var mt={en:Object(tt["a"])(Object(tt["a"])({},dt),ct.a),zh:Object(tt["a"])(Object(tt["a"])({},pt),ht.a)};function ft(){var t=localStorage.getItem("language");if(t)return t;for(var e=(navigator.language||navigator.browserLanguage).toLowerCase(),n=Object.keys(mt),s=0,o=n;s<o.length;s++){var i=o[s];if(e.indexOf(i)>-1)return i}return"zh"}var bt=new it["a"]({locale:ft(),messages:mt,silentTranslationWarn:!0});at.a.i18n((function(t,e){return bt.t(t,e)}));var gt=bt,vt={sshInfo:{host:"192.168.76.66",username:"root",port:22,password:"",logintype:0},sshList:Object.prototype.hasOwnProperty.call(localStorage,"sshList")?localStorage.getItem("sshList"):null,termList:[],currentTab:{},shouldValidToken:!0,token:"",ctx:void 0,language:ft()},wt=(n("5319"),{sshReq:function(t){return window.btoa('{\n            "username":"'.concat(t.sshInfo.username,'",\n            "ipaddress":"').concat(t.sshInfo.host,'",\n            "port":').concat(t.sshInfo.port,',\n            "password":"').concat(t.sshInfo.password.replace(/[\n]/g,"\\n"),'",\n            "logintype":').concat(void 0===t.sshInfo.logintype?0:t.sshInfo.logintype,"\n        }"))},token:function(t){return t.token},ctx:function(t){return t.ctx}}),yt={setLanguage:function(t,e){var n=t.commit;n("SET_LANGUAGE",e)},setToken:function(t,e){var n=t.commit;n("SET_TOKEN",e)}},kt={SET_PASS:function(t,e){t.sshInfo.password=e},SET_LIST:function(t,e){t.sshList=e,localStorage.setItem("sshList",e)},SET_TERMLIST:function(t,e){t.termList=e},SET_SSH:function(t,e){t.sshInfo.host=e.host,t.sshInfo.username=e.username,t.sshInfo.port=e.port,t.sshInfo.logintype=e.logintype,void 0!==e.password&&(t.sshInfo.password=e.password)},SET_TAB:function(t,e){t.currentTab=e},SET_LANGUAGE:function(t,e){t.language=e,localStorage.setItem("language",e)},SET_TOKEN:function(t,e){t.token=e}};s["default"].use(ot["a"]);var Tt=new ot["a"].Store({state:vt,getters:wt,mutations:kt,actions:yt});function St(t){switch(t){case 400:i.a.error("请求出错");break;case 403:i.a.warning({message:"拒绝访问"});break;case 404:i.a.warning({message:"请求错误,未找到该资源"});break;case 500:i.a.warning({message:"服务端错误"});break}return t>=200&&t<300}function $t(){var t=Tt.getters.ctx?Tt.getters.ctx:"/";return console.log("baseUrl:"+t),t}var xt=st["a"].create({timeout:8e3,baseURL:$t(),validateStatus:St});xt.interceptors.request.use(function(){var t=Object(nt["a"])(Object(et["a"])().mark((function t(e){return Object(et["a"])().wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,$t();case 2:return e.baseURL=t.sent,t.abrupt("return",e);case 4:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}(),(function(t){return Promise.reject(t)})),xt.interceptors.response.use((function(t){return t.data}),(function(t){return void 0===t.response&&i.a.error("连接服务器失败"),Promise.reject(t.response)}));var _t=xt;function Lt(t,e){return _t.get("/webssh/check?sshInfo=".concat(t,"&token=").concat(e))}function It(){return _t.get("/webssh/shouldVerifyToken")}function Ct(t){return _t({url:"/webssh/login",method:"post",data:t})}var Ot=function(){var t=this,e=t._self._c;return e("div",{staticClass:"MainContainer"},[e("el-button",{attrs:{type:"primary",size:"small"},on:{click:function(e){t.getFileList(),t.dialogVisible=!0}}},[t._v(t._s(t.$t("FileBrowser")))]),e("el-dialog",{attrs:{title:t.$t("FileBrowser")+"("+this.$store.state.sshInfo.host+")",visible:t.dialogVisible,top:"5vh",width:t.dialogWidth},on:{"update:visible":function(e){t.dialogVisible=e}}},[e("el-row",[e("el-col",{attrs:{span:this.pathSpan}},[e("el-input",{nativeOn:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.getFileList()}},model:{value:t.currentPath,callback:function(e){t.currentPath=e},expression:"currentPath"}})],1),e("el-col",{attrs:{span:6}},[e("el-button-group",[e("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-arrow-up"},on:{click:function(e){return t.upDirectory()}}}),e("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-refresh"},on:{click:function(e){return t.getFileList()}}}),e("el-dropdown",{on:{click:function(e){return t.openUploadDialog()},command:t.handleUploadCommand}},[e("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-upload"}}),e("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[e("el-dropdown-item",{attrs:{command:"file"}},[t._v(t._s(t.$t("uploadFile")))]),e("el-dropdown-item",{attrs:{command:"folder"}},[t._v(t._s(t.$t("uploadFolder")))])],1)],1)],1),e("el-dialog",{attrs:{"custom-class":"uploadContainer",title:t.$t(this.titleTip),visible:t.uploadVisible,"append-to-body":"",width:t.uploadWidth},on:{"update:visible":function(e){t.uploadVisible=e}}},[e("el-upload",{ref:"upload",attrs:{multiple:"",drag:"",action:t.uploadUrl,data:t.uploadData,"before-upload":t.beforeUpload,"on-progress":t.uploadProgress,"on-success":t.uploadSuccess}},[e("i",{staticClass:"el-icon-upload"}),e("div",{staticClass:"el-upload__text"},[t._v(t._s(t.$t(this.selectTip)))]),e("div",{staticClass:"el-upload__tip",attrs:{slot:"tip"},slot:"tip"},[t._v(t._s(this.uploadTip))])])],1)],1)],1),e("el-table",{attrs:{data:t.fileList,height:t.clientHeight},on:{"row-click":t.rowClick}},[e("el-table-column",{attrs:{label:t.$t("Name"),width:t.nameWidth,sortable:"","sort-method":t.nameSort},scopedSlots:t._u([{key:"default",fn:function(n){return[!0===n.row.IsDir?e("p",{staticClass:"el-icon-folder",staticStyle:{color:"#0c60b5",cursor:"pointer"}},[t._v(" "+t._s(n.row.Name))]):!1===n.row.IsDir?e("p",{staticClass:"el-icon-document",staticStyle:{cursor:"pointer"}},[t._v(" "+t._s(n.row.Name))]):t._e()]}}])}),e("el-table-column",{attrs:{label:t.$t("PermissionsString"),prop:"PermissionsString"}}),e("el-table-column",{attrs:{label:t.$t("OwnerName"),prop:"OwnerName"}}),e("el-table-column",{attrs:{label:t.$t("GroupName"),prop:"GroupName"}}),e("el-table-column",{attrs:{label:t.$t("Size"),prop:"Size"}}),e("el-table-column",{attrs:{label:t.$t("ModifiedTime"),prop:"ModifyTime",sortable:"",width:"150"}})],1)],1)],1)},Pt=[];n("b0c0"),n("fb6a"),n("a15b");function Et(t,e,n){return _t.get("/webssh/file/list?path=".concat(t,"&sshInfo=").concat(e,"&token=").concat(n),{timeout:3e4})}var Ft={name:"FileList",data:function(){return{uploadVisible:!1,dialogVisible:!1,fileList:[],downloadFilePath:"",currentPath:"",pathSpan:18,clientHeight:0,selectTip:"clickSelectFile",titleTip:"uploadFile",uploadTip:"",dialogWidth:"70%",uploadWidth:"32%",nameWidth:120,progressPercent:0}},created:function(){this.resizeDialog()},mounted:function(){var t=this;this.resizeDialog(),window.onresize=function(){t.resizeDialog()}},computed:Object(tt["a"])(Object(tt["a"])({},Object(ot["b"])(["currentTab"])),{},{uploadUrl:function(){return"".concat("".concat(location.origin),"/webssh/file/upload")},uploadData:function(){return{sshInfo:this.$store.getters.sshReq,path:this.currentPath,token:this.$store.getters.token}}}),watch:{currentTab:function(){this.fileList=[],this.currentPath=this.currentTab&&this.currentTab.path}},methods:{resizeDialog:function(){var t=document.body.clientWidth;this.clientHeight=document.body.clientHeight-200,this.pathSpan=18,t<600?(this.dialogWidth="98%",this.uploadWidth="100%",this.nameWidth=120,this.pathSpan=16,this.clientHeight=document.body.clientHeight-205):t>=600&&t<1e3?(this.dialogWidth="80%",this.uploadWidth="59%",this.nameWidth=220):(this.dialogWidth="50%",this.uploadWidth="28%",this.nameWidth=220)},openUploadDialog:function(){this.uploadTip="".concat(this.$t("uploadPath"),": ").concat(this.currentPath),this.uploadVisible=!0},handleUploadCommand:function(t){"folder"===t?(this.selectTip="clickSelectFolder",this.titleTip="uploadFolder"):(this.selectTip="clickSelectFile",this.titleTip="uploadFile"),this.openUploadDialog();var e="folder"===t,n=this.webkitdirectorySupported();n?this.$nextTick((function(){var t=document.getElementsByClassName("el-upload__input")[0];t&&(t.webkitdirectory=e)})):e&&this.$message.warning("当前浏览器不支持")},webkitdirectorySupported:function(){return"webkitdirectory"in document.createElement("input")},beforeUpload:function(t){this.uploadTip="".concat(this.$t("uploading")," ").concat(t.name," ").concat(this.$t("to")," ").concat(this.currentPath,", ").concat(this.$t("notCloseWindows"),".."),this.uploadData.id=t.uid;var e=t.webkitRelativePath;return this.uploadData.dir=e?e.substring(0,e.lastIndexOf("/")):"",!0},uploadSuccess:function(t,e){this.uploadTip="".concat(e.name).concat(this.$t("uploadFinish"),"!")},uploadProgress:function(t,e){t.percent=t.percent/2,e.percentage=e.percentage/2;var n=this.$store.getters.token;if(e.startWsProgree=!1,!e.startWsProgree&&t.percent>=10&&(e.startWsProgree=!0),50===t.percent){var s=new WebSocket("".concat("http:"===location.protocol?"ws":"wss","://").concat(location.host).concat(Tt.getters.ctx?Tt.getters.ctx:"/","/webssh/file/progress?token=").concat(n,"&id=").concat(e.uid));s.onmessage=function(t){e.percentage=(e.size+Number(t.data))/(2*e.size)*100},s.onclose=function(){console.log(Date(),"onclose")},s.onerror=function(){console.log(Date(),"onerror")}}},nameSort:function(t,e){return t.Name>e.Name},rowClick:function(t){t.IsDir?(this.currentPath="/"===this.currentPath.charAt(this.currentPath.length-1)?this.currentPath+t.Name:this.currentPath+"/"+t.Name,this.getFileList()):(this.downloadFilePath="/"===this.currentPath.charAt(this.currentPath.length-1)?this.currentPath+t.Name:this.currentPath+"/"+t.Name,this.downloadFile())},getFileList:function(){var t=this;return Object(nt["a"])(Object(et["a"])().mark((function e(){var n;return Object(et["a"])().wrap((function(e){while(1)switch(e.prev=e.next){case 0:return t.currentPath=t.currentPath.replace(/\/+/g,"/"),""===t.currentPath&&(t.currentPath="/"),e.next=4,Et(t.currentPath,t.$store.getters.sshReq,t.$store.state.token);case 4:n=e.sent,"success"===n.Msg?(null===n.Data.list?t.fileList=[]:t.fileList=n.Data.list,t.updatePath(t.currentPath)):(t.fileList=[],t.$message.error(n.Msg),t.updatePath("/"));case 6:case"end":return e.stop()}}),e)})))()},upDirectory:function(){if("/"!==this.currentPath){var t=this.currentPath.split("/");t=""===t[t.length-1]?t.slice(0,t.length-2):t.slice(0,t.length-1),this.currentPath=1===t.length?"/":t.join("/"),this.getFileList()}},downloadFile:function(){var t=this.$store.getters.token,e="".concat(location.origin),n="".concat(e,"/webssh/file/download?path=").concat(this.downloadFilePath,"&sshInfo=").concat(this.$store.getters.sshReq,"&token=").concat(t);window.open(n)},updatePath:function(t){for(var e=this.$store.state.termList,n=0;n<e.length;++n)if(e[n].name===this.currentTab.name){e[n].path=t;break}this.$store.commit("SET_TERMLIST",e)}}},jt=Ft,zt=(n("3eca"),n("2877")),Mt=Object(zt["a"])(jt,Ot,Pt,!1,null,null,null),Nt=Mt.exports,Dt={components:{"file-list":Nt},data:function(){return{foreShowLogin:!1,loginLoading:!1,textareaVisible:!1,login:{form:{username:"test",password:"test@123!"},checkRules:{username:[{required:!0,trigger:"blur"}],password:[{required:!0,trigger:"blur",message:"password is required"}]}},checkRules:{host:[{required:!0,trigger:"blur"}],port:[{required:!0,trigger:"blur",type:"number",transform:function(t){return Number(t)}}],username:[{required:!0,trigger:"blur"}],password:[{required:!0,trigger:"blur",message:"value is required"}]}}},methods:{handleLogout:function(){var t=this;t.foreShowLogin=!1,t.$emit("ssh-logout"),t.$store.dispatch("setToken","")},handleLogin:function(){var t=this;this.$refs.loginForm.validate((function(e){e&&!t.loginLoading&&(t.loginLoading=!0,Ct(t.login.form).then((function(e){if(t.loginLoading=!1,"200"==e.code){var n=e.Data.token;n&&t.$store.dispatch("setToken",n)}})))})),setTimeout((function(){t.loginLoading&&(t.loginLoading=!1)}),10)},handleSetLanguage:function(){var t=ft(),e="zh"===t?"en":"zh";this.$i18n.locale=e,this.$store.dispatch("setLanguage",e)},handleCommand:function(t){this.$store.commit("SET_SSH",t),void 0===t.password&&this.$store.commit("SET_PASS",""),this.$emit("ssh-select")},cleanHistory:function(t){var e=this.sshList;e.forEach((function(n,s){n.host===t.host&&e.splice(s,1)})),this.$store.commit("SET_LIST",window.btoa(JSON.stringify(e)))},handleChangePKFile:function(t){var e=t.target.files[0];if(e){var n=this.sshInfo,s=new FileReader;s.onload=function(t){n.password=t.target.result},s.readAsText(e)}},getRequestParam:function(t){if(console.log("request query:"+location.search),t=new RegExp("[?&]"+encodeURIComponent(t)+"=([^&]*)").exec(location.search))return decodeURIComponent(t[1])}},mounted:function(){if(this.sshList.length>0){var t=this.sshList[this.sshList.length-1];this.$store.commit("SET_SSH",t),void 0===t.password&&this.$store.commit("SET_PASS","")}var e=this,n=e.getRequestParam("prefix");console.log("contextPath:"+n),n&&(e.$store.state.ctx=n),It().then((function(t){e.$store.state.shouldValidToken=t.Data}))},computed:Object(tt["a"])(Object(tt["a"])({},Object(ot["b"])(["sshInfo"])),{},{privateKey:function(){return 1===this.sshInfo.logintype},showLogin:function(){var t=this.$store.state.shouldValidToken,e=this.$store.state.token;return!(!t||""!=e&&null!=e&&void 0!=e)||!!this.foreShowLogin},showLogout:function(){var t=this.$store.state.shouldValidToken,e=this.$store.state.token;return!(!t||""==e||null==e||void 0==e)},sshList:function(){var t=this.$store.state.sshList;return null===t?[]:JSON.parse(window.atob(t))}})},Rt=Dt,Vt=Object(zt["a"])(Rt,Q,Z,!1,null,"48914d88",null),Wt=Vt.exports,Ht=function(){var t=this,e=t._self._c;return e("div",[e("el-tabs",{attrs:{type:"card"},on:{"tab-remove":t.removeTab,"tab-click":t.clickTab},model:{value:t.currentTerm,callback:function(e){t.currentTerm=e},expression:"currentTerm"}},t._l(t.termList,(function(t,n){return e("el-tab-pane",{key:t.name,attrs:{label:t.label,name:t.name,closable:t.closable}},[e("terminal",{ref:t.name,refInFor:!0,attrs:{id:"Terminal"+n}})],1)})),1),e("div",{directives:[{name:"show",rawName:"v-show",value:t.contextMenuVisible,expression:"contextMenuVisible"}]},[e("ul",{staticClass:"contextmenu",style:{left:t.left+"px",top:t.top+"px"}},[e("li",{on:{click:function(e){return t.copyTab()}}},[e("el-button",{attrs:{type:"text",size:"mini"}},[t._v(t._s(t.$t("Copy")))])],1),e("li",{on:{click:function(e){return t.lockTab()}}},[e("el-button",{attrs:{type:"text",size:"mini"}},[t._v(t._s(t.lockButtonShow(t.menuTab)))])],1),e("li",{on:{click:function(e){return t.setScreenfull()}}},[e("el-button",{attrs:{type:"text",size:"mini"}},[t._v(t._s(t.$t("FullScreen")))])],1),e("li",{on:{click:function(e){return t.removeTab(t.menuTab)}}},[e("el-button",{attrs:{type:"text",size:"mini"}},[t._v(t._s(t.$t("Close")))])],1),e("el-divider"),e("li",{on:{click:function(e){return t.renameTab()}}},[e("el-button",{attrs:{type:"text",size:"mini"}},[t._v(t._s(t.$t("Rename")))])],1),e("el-divider"),e("li",{on:{click:function(e){return t.closeTabs("left")}}},[e("el-button",{attrs:{type:"text",size:"mini"}},[t._v(t._s(t.$t("CloseLeft")))])],1),e("li",{on:{click:function(e){return t.closeTabs("right")}}},[e("el-button",{attrs:{type:"text",size:"mini"}},[t._v(t._s(t.$t("CloseRight")))])],1),e("li",{on:{click:function(e){return t.closeTabs("other")}}},[e("el-button",{attrs:{type:"text",size:"mini"}},[t._v(t._s(t.$t("CloseOther")))])],1),e("li",{on:{click:function(e){return t.closeTabs("all")}}},[e("el-button",{attrs:{type:"text",size:"mini"}},[t._v(t._s(t.$t("CloseAll")))])],1)],1)])],1)},At=[],qt=(n("9e1f"),n("6ed5")),Kt=n.n(qt),Ut=n("b85c"),Bt=(n("4de4"),n("14d9"),n("aa47")),Gt=n("93bf"),Jt=n.n(Gt),Xt=function(){var t=this,e=t._self._c;return e("div",[e("div",{attrs:{id:t.id}})])},Yt=[],Qt=n("fcf3"),Zt=n("47d0"),te=n("173c"),ee={name:"Terminal",props:["id"],data:function(){return{term:null,ws:null,resetClose:!1,ssh:null,savePass:!1,fontSize:15}},mounted:function(){this.createTerm()},methods:{setSSH:function(){this.$store.commit("SET_SSH",this.ssh)},resizeTerm:function(t){var e=document.body.clientWidth;t.style.height=e<600?document.body.clientHeight-301+"px":e>=600&&e<1e3?document.body.clientHeight-151+"px":document.body.clientHeight-101+"px"},createTerm:function(){var t=this;if(""!==this.$store.state.sshInfo.password){var e=document.getElementById(this.id);this.resizeTerm(e);var n=this.$store.getters.sshReq,s=this.$store.getters.token;this.close();var o=Tt.getters.ctx?Tt.getters.ctx:"/",i=new Zt["FitAddon"];this.term=new Qt["Terminal"]({rendererType:"canvas",convertEol:!0,disableStdin:!1,cursorBlink:!0,cursorStyle:"block",logLevel:"debug",scrollback:800,windowsMode:!0}),this.term.loadAddon(i),this.term.open(document.getElementById(this.id));try{i.fit()}catch(d){}var r=this,a={timeout:15e3,intervalObj:null,stop:function(){clearInterval(this.intervalObj)},start:function(){this.intervalObj=setInterval((function(){null!==r.ws&&1===r.ws.readyState&&r.ws.send("ping")}),this.timeout)}},l="已超时关闭!";"en"===this.$store.state.language&&(l="Connection timed out!"),this.ws=new WebSocket("".concat("http:"===location.protocol?"ws":"wss","://").concat(location.host).concat(o,"/webssh/term?token=").concat(s,"&sshInfo=").concat(n,"&rows=").concat(this.term.rows,"&cols=").concat(this.term.cols,"&closeTip=").concat(l)),this.ws.onopen=function(){console.log(Date(),"onopen"),r.connected(),a.start()},this.ws.onclose=function(){console.log(Date(),"onclose"),r.resetClose||(t.savePass||(t.$store.commit("SET_PASS",""),t.ssh.password=""),t.$message({message:t.$t("wsClose"),type:"warning",duration:0,showClose:!0}),t.ws=null),a.stop(),r.resetClose=!1},this.ws.onerror=function(){console.log(Date(),"onerror")};var c=new te["AttachAddon"](this.ws);this.term.loadAddon(c);var u=this.term;u.focus(),this.term.attachCustomKeyEventHandler((function(t){var e=["F5","F11","F12"];return!(e.indexOf(t.key)>-1)&&(t.ctrlKey&&"v"===t.key||t.ctrlKey&&"c"===t.key&&r.term.hasSelection()?(document.execCommand("copy"),!1):void 0)}));var h="onwheel"in document.createElement("div")?"wheel":void 0!==document.onmousewheel?"mousewheel":"DOMMouseScroll";e.addEventListener(h,(function(e){if(e.ctrlKey){e.preventDefault(),e.deltaY<0?r.term.setOption("fontSize",++t.fontSize):r.term.setOption("fontSize",--t.fontSize);try{i.fit()}catch(e){}null!==r.ws&&1===r.ws.readyState&&r.ws.send("resize:".concat(r.term.rows,":").concat(r.term.cols))}})),window.addEventListener("resize",(function(){r.resizeTerm(e);try{i.fit()}catch(d){}null!==r.ws&&1===r.ws.readyState&&r.ws.send("resize:".concat(r.term.rows,":").concat(r.term.cols))}))}},connected:function(){var t=this;return Object(nt["a"])(Object(et["a"])().mark((function e(){var n,s,o,i;return Object(et["a"])().wrap((function(e){while(1)switch(e.prev=e.next){case 0:return n=t.$store.state.sshInfo,t.ssh=Object.assign({},n),e.next=4,Lt(t.$store.getters.sshReq,t.$store.state.token);case 4:if(s=e.sent,"success"===s.Msg){e.next=9;break}return e.abrupt("return");case 9:t.savePass=s.Data.savePass;case 10:document.title=n.host,o=t.$store.state.sshList,null===o?o=t.savePass?'[{"host": "'.concat(n.host,'", "username": "').concat(n.username,'", "port":').concat(n.port,', "logintype":').concat(n.logintype,', "password":"').concat(n.password,'"}]'):'[{"host": "'.concat(n.host,'", "username": "').concat(n.username,'", "port":').concat(n.port,',  "logintype":').concat(n.logintype,"}]"):(i=JSON.parse(window.atob(o)),i.forEach((function(t,e){t.host===n.host&&t.logintype===n.logintype&&i.splice(e,1)})),i.push({host:n.host,username:n.username,port:n.port,logintype:n.logintype}),t.savePass&&(i[i.length-1].password=n.password),o=JSON.stringify(i)),t.$store.commit("SET_LIST",window.btoa(o));case 14:case"end":return e.stop()}}),e)})))()},close:function(){null!==this.ws&&(this.ws.close(),this.resetClose=!0),null!==this.term&&this.term.dispose()}},beforeDestroy:function(){this.close()}},ne=ee,se=Object(zt["a"])(ne,Xt,Yt,!1,null,null,null),oe=se.exports,ie={name:"Tabs",components:{terminal:oe},data:function(){return{currentTermId:"",currentTerm:"",currentTermIndex:0,menuTab:"",contextMenuVisible:!1,left:"",top:""}},computed:{termList:{get:function(){return this.$store.state.termList},set:function(t){this.$store.commit("SET_TERMLIST",t)}}},watch:{contextMenuVisible:function(){this.contextMenuVisible?document.body.addEventListener("click",this.closeContextMenu):document.body.removeEventListener("click",this.closeContextMenu)}},mounted:function(){for(var t=document.body.getElementsByClassName("el-tabs__nav-scroll"),e=0;e<t.length;++e)t[e].oncontextmenu=this.openContextMenu;var n=this,s=document.querySelector(".el-tabs__nav");Bt["a"].create(s,{animation:200,onEnd:function(t){var e=t.newIndex,s=t.oldIndex,o=n.termList.splice(s,1)[0];n.termList.splice(e,0,o)}})},methods:{lockButtonShow:function(t){if(this.termList.length>0&&""!==t){var e=this.termList.filter((function(e){return e.name===t}))[0];return void 0===e||e.closable?this.$t("Lock"):this.$t("Unlock")}return this.$t("Lock")},copyTab:function(){this.$refs["".concat(this.menuTab)][0].setSSH(),this.openTerm()},setScreenfull:function(){if(!Jt.a.isEnabled)return this.$message({message:"not support fullscreen",type:"warning"}),!1;Jt.a.toggle()},getCurrMenuIndex:function(t){var e=this,n=0;return this.termList.forEach((function(s,o){(t&&s.name===t.name||s.name===e.menuTab)&&(n=o)})),n},closeTabs:function(t){var e=this,n=function(){e.currentTermIndex=o;var t=e.termList[o];e.currentTerm=t.name,document.title=t.label,e.$store.commit("SET_TAB",e.termList[e.currentTermIndex]),e.$refs["".concat(t.name)][0].setSSH()},s=function(t,n){var s=[];e.termList.forEach((function(e,o){(o>=t&&o<n||!e.closable)&&s.push(e)})),e.termList=s},o=this.getCurrMenuIndex();switch(t){case"left":this.currentTermIndex<o&&n(),s(o,this.termList.length);break;case"right":this.currentTermIndex>o&&n(),s(0,o+1);break;case"other":this.currentTermIndex!==o&&n(),s(o,o+1);break;case"all":s(-1,-1),o=this.getCurrMenuIndex(),n();break}this.closeContextMenu()},closeContextMenu:function(){this.contextMenuVisible=!1},openContextMenu:function(t){t.preventDefault();var e=t.srcElement?t.srcElement:t.target;e.id&&(this.menuTab=e.id.substr(4),this.contextMenuVisible=!0,this.left=t.clientX,this.top=20)},genID:function(t){return Number(Math.random().toString().substr(3,t)+Date.now()).toString(36)},openTerm:function(){var t=this.$store.state.sshInfo;if(""!==t.password){var e="".concat(this.genID(10));this.termList.push({name:"".concat(t.host,"-").concat(e),label:t.host,path:"/",closable:!0,id:e});var n=this.termList[this.termList.length-1];this.currentTerm=n.name,this.currentTermId=n.id,this.currentTermIndex=this.termList.length-1,this.$store.commit("SET_TAB",this.termList[this.currentTermIndex])}},findTerm:function(t){this.currentTermIndex=this.getCurrMenuIndex(t),this.$store.commit("SET_TAB",this.termList[this.currentTermIndex])},clickTab:function(t){this.$refs["".concat(t.name)][0].setSSH(),document.title=t.label,this.findTerm(t)},removeTab:function(t){for(var e=this.currentTerm,n=0;n<this.termList.length;++n)if(t==this.termList[n].name){if(!this.termList[n].closable)return void this.$message({message:this.$t("unlockClose"),type:"warning"});var s=this.termList[n+1]||this.termList[n-1];s&&(e=s.name)}this.currentTerm=e,this.$refs["".concat(this.currentTerm)][0].setSSH(),this.termList=this.termList.filter((function(e){return e.name!==t})),this.findTerm()},renameTab:function(){var t=this;return Object(nt["a"])(Object(et["a"])().mark((function e(){var n,s,o,i,r;return Object(et["a"])().wrap((function(e){while(1)switch(e.prev=e.next){case 0:n=Object(Ut["a"])(t.termList),e.prev=1,n.s();case 3:if((s=n.n()).done){e.next=15;break}if(o=s.value,o.name!==t.menuTab){e.next=13;break}return e.next=8,Kt.a.prompt("",t.$t("Rename"),{inputValue:o.label,inputErrorMessage:"please input value",inputValidator:function(t){return null!==t&&t.length>0}}).catch(null);case 8:return i=e.sent,r=i.value,o.label=r,t.currentTerm===t.menuTab&&(document.title=o.label),e.abrupt("break",15);case 13:e.next=3;break;case 15:e.next=20;break;case 17:e.prev=17,e.t0=e["catch"](1),n.e(e.t0);case 20:return e.prev=20,n.f(),e.finish(20);case 23:case"end":return e.stop()}}),e,null,[[1,17,20,23]])})))()},lockTab:function(){var t,e=Object(Ut["a"])(this.termList);try{for(e.s();!(t=e.n()).done;){var n=t.value;n.name===this.menuTab&&(n.closable=!n.closable)}}catch(s){e.e(s)}finally{e.f()}}}},re=ie,ae=(n("5502"),Object(zt["a"])(re,Ht,At,!1,null,"51dadbd7",null)),le=ae.exports,ce={name:"App",components:{vheader:Wt,tabs:le}},ue=ce,he=(n("0c06"),Object(zt["a"])(ue,X,Y,!1,null,null,null)),de=he.exports;n("abb2");s["default"].use(J),s["default"].config.productionTip=!1,new s["default"]({el:"#app",i18n:gt,store:Tt,render:function(t){return t(de)}})},8533:function(t,e,n){},b20f:function(t,e,n){},d7b7:function(t,e,n){}});