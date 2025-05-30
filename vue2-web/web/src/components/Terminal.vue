<template>
    <div>
        <div :id="id">

        </div>

    </div>
</template>

<script>
import {checkSSH} from '@/api/common'
import {Terminal} from 'xterm'
import {FitAddon} from 'xterm-addon-fit'
import {AttachAddon} from 'xterm-addon-attach'
import store from '@/store';
import wshelper from '@/utils/ws';

export default {
    name: 'Terminal',
    props: ['id'],
    data() {
        return {
            term: null,
            ws: null,
            resetClose: false,
            ssh: null,
            savePass: false,
            fontSize: 15
        }
    },
    mounted() {
        this.createTerm()
    },
    methods: {
        setSSH() {
            this.$store.commit('SET_SSH', this.ssh)
        },
        resizeTerm(termWeb) {
            const clientWidth = document.body.clientWidth
            if (clientWidth < 600) {
                termWeb.style.height = (document.body.clientHeight - 301) + 'px'
            } else if (clientWidth >= 600 && clientWidth < 1000) {
                termWeb.style.height = (document.body.clientHeight - 151) + 'px'
            } else {
                termWeb.style.height = (document.body.clientHeight - 101) + 'px'
            }
        },
        createTerm() {
            if (this.$store.state.sshInfo.password === '') {
                return
            }
            const termWeb = document.getElementById(this.id)
            this.resizeTerm(termWeb)
            const sshReq = this.$store.getters.sshReq
            const token = this.$store.getters.token
            this.close()
            const prefix = (process.env.NODE_ENV === 'production' ? '' : '/ws') + (store.getters.ctx ? store.getters.ctx : '')
            const fitAddon = new FitAddon()
            this.term = new Terminal({
                rendererType: 'canvas', //渲染类型
                convertEol: true, //启用时，光标将设置为下一行的开头
                disableStdin: false, //是否应禁用输入。
                cursorBlink: true,
                cursorStyle: 'block',
                logLevel: 'info',
                scrollback: 800,
                windowsMode: true
            })
            this.term.loadAddon(fitAddon)
            this.term.open(document.getElementById(this.id))
            try {
                fitAddon.fit()
            } catch (e) {/**/
            }
            const self = this
            const heartCheck = {
                timeout: 15000, // 15s发一次心跳
                intervalObj: null,
                stop: function () {
                    clearInterval(this.intervalObj)
                },
                start: function () {
                    this.intervalObj = setInterval(function () {
                        if (self.ws !== null && self.ws.readyState === 1) {
                            self.ws.send('ping')
                        }
                    }, this.timeout)
                }
            }
            let closeTip = '已超时关闭!'
            if (this.$store.state.language === 'en') {
                closeTip = 'Connection timed out!'
            }
            // open websocket
            this.ws = wshelper.newWs({url:`${(location.protocol === 'http:' ? 'ws' : 'wss')}://${location.host}${prefix}/webssh/term?token=${token}&sshInfo=${sshReq}&rows=${this.term.rows}&cols=${this.term.cols}&closeTip=${closeTip}`})
            this.ws.onopen = () => {
                console.log(Date(), 'onopen')
                self.connected()
                heartCheck.start()
            }
            this.ws.onclose = () => {
                console.log(Date(), 'onclose')
                if (!self.resetClose) {
                    if (!this.savePass) {
                        this.$store.commit('SET_PASS', '')
                        this.ssh.password = ''
                    }
                    this.$message({
                        message: this.$t('wsClose'),
                        type: 'warning',
                        duration: 0,
                        showClose: true
                    })
                    this.ws = null
                }
                heartCheck.stop()
                self.resetClose = false
            }
            this.ws.onerror = () => {
                console.log(Date(), 'onerror')
            }
            const attachAddon = new AttachAddon(this.ws)
            this.term.loadAddon(attachAddon)
            let term = this.term;
            term.focus();
            this.term.attachCustomKeyEventHandler((e) => {
                const keyArray = ['F5', 'F11', 'F12']
                if (keyArray.indexOf(e.key) > -1) {
                    return false
                }
                // ctrl + v
                if (e.ctrlKey && e.key === 'v') {
                    document.execCommand('copy')
                    return false
                }
                // ctrl + c
                if (e.ctrlKey && e.key === 'c' && self.term.hasSelection()) {
                    document.execCommand('copy')
                    return false
                }
            })
            // detect available wheel event
            // 各个厂商的高版本浏览器都支持"wheel"
            // Webkit 和 IE一定支持"mousewheel"
            // "DOMMouseScroll" 用于低版本的firefox
            const wheelSupport = 'onwheel' in document.createElement('div') ? 'wheel' : document.onmousewheel !== undefined ? 'mousewheel' : 'DOMMouseScroll'
            termWeb.addEventListener(wheelSupport, (e) => {
                if (e.ctrlKey) {
                    e.preventDefault()
                    if (e.deltaY < 0) {
                        self.term.setOption('fontSize', ++this.fontSize)
                    } else {
                        self.term.setOption('fontSize', --this.fontSize)
                    }
                    try {
                        fitAddon.fit()
                    } catch (e) {/**/
                    }
                    if (self.ws !== null && self.ws.readyState === 1) {
                        self.ws.send(`resize:${self.term.rows}:${self.term.cols}`)
                    }
                }
            })
            window.addEventListener('resize', () => {
                self.resizeTerm(termWeb)
                try {
                    fitAddon.fit()
                } catch (e) {/**/
                }
                if (self.ws !== null && self.ws.readyState === 1) {
                    self.ws.send(`resize:${self.term.rows}:${self.term.cols}`)
                }
            })

        },
        async connected() {
            const sshInfo = this.$store.state.sshInfo
            // 深度拷贝对象
            this.ssh = Object.assign({}, sshInfo)
            // 校验ssh连接信息是否正确
            const result = await checkSSH(this.$store.getters.sshReq, this.$store.state.token)
            if (result.msg !== 'success') {
                return
            } else {
                this.savePass = result.data.savePass
            }
            document.title = sshInfo.host
            let sshList = this.$store.state.sshList
            let tempRdpConfig = sshInfo.rdpConfig
            if (sshList === null) {
                if (this.savePass) {
                    sshList = `[{"host": "${sshInfo.host}", "username": "${sshInfo.username}", "port":${sshInfo.port}, "logintype":${sshInfo.logintype}, "password":"${sshInfo.password}","rdpConfig":"${tempRdpConfig}"}]`
                } else {
                    sshList = `[{"host": "${sshInfo.host}", "username": "${sshInfo.username}", "port":${sshInfo.port},  "logintype":${sshInfo.logintype},"rdpConfig":"${tempRdpConfig}"}]`
                }
            } else {
                const sshListObj = JSON.parse(window.atob(sshList))
                sshListObj.forEach((v, i) => {
                    if (v.host === sshInfo.host && v.logintype === sshInfo.logintype
                        && v.rdpConfig.rdp == sshInfo.rdpConfig.rdp &&  v.rdpConfig.directConnectRdpServer == sshInfo.rdpConfig.directConnectRdpServer) {
                        sshListObj.splice(i, 1)
                    }
                })
                sshListObj.push({
                    host: sshInfo.host,
                    username: sshInfo.username,
                    port: sshInfo.port,
                    logintype: sshInfo.logintype,
                    rdpConfig: tempRdpConfig
                })
                if (this.savePass) {
                    sshListObj[sshListObj.length - 1].password = sshInfo.password
                }
                sshList = JSON.stringify(sshListObj)
            }
            this.$store.commit('SET_LIST', window.btoa(sshList))
        },
        close() {
            if (this.ws !== null) {
                this.ws.close()
                this.resetClose = true
            }
            if (this.term !== null) {
                this.term.dispose()
            }
        }
    },
    beforeDestroy() {
        this.close()
    }
}
</script>
