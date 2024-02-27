<template>
    <div>
        <el-row>
            <el-col>
                <el-form :inline="true" style="padding-top: 10px;" :model="sshInfo" :rules="checkRules">
                    <el-form-item size="small" prop="host">
                        <template slot="label">
                            <el-tooltip effect="dark" placement="left">
                                <div slot="content">
                                    <p>Switch Language</p>
                                </div>
                                <span @click="handleSetLanguage()">Host</span>
                            </el-tooltip>
                        </template>
                        <el-input v-model="sshInfo.host" :placeholder="$t('hostTip')"
                                  @keyup.enter.native="$emit('ssh-select')"></el-input>
                    </el-form-item>
                    <el-form-item label="Port" size="small" prop="port">
                        <el-input v-model="sshInfo.port" :placeholder="$t('portTip')"
                                  @keyup.enter.native="$emit('ssh-select')" style="width: 100px"></el-input>
                    </el-form-item>
                    <el-form-item label="Username" size="small" prop="username">
                        <el-input v-model="sshInfo.username" :placeholder="$t('nameTip')"
                                  @keyup.enter.native="$emit('ssh-select')" style="width: 110px"></el-input>
                    </el-form-item>
                    <el-form-item size="small" prop="password">
                        <template slot="label">
                            <el-tooltip effect="dark" placement="left">
                                <div slot="content">
                                    <p>{{ `Switch to ${this.privateKey ? 'Password' : 'PrivateKey'} login` }}</p>
                                </div>
                                <span @click="sshInfo.logintype === 0 ? sshInfo.logintype=1: sshInfo.logintype=0">{{
                                        privateKey ? 'PrivateKey' : 'Password'
                                    }}</span>
                            </el-tooltip>
                        </template>
                        <el-input v-model="sshInfo.password" @click.native="textareaVisible=privateKey"
                                  @keyup.enter.native="$emit('ssh-select')"
                                  :placeholder="$t('inputTip') + `${this.privateKey ? $t('privateKey') : $t('password')}`"
                                  show-password></el-input>
                    </el-form-item>
                    <el-dialog :title="$t('privateKey')" :visible.sync="textareaVisible" :close-on-click-modal="false">
                        <el-input :rows="8" v-model="sshInfo.password" type="textarea"
                                  :placeholder="$t('keyTip')"></el-input>
                        <div slot="footer" class="dialog-footer">
                            <!-- 选择密钥文件 -->
                            <input ref="pkFile" @change="handleChangePKFile" type="file"
                                   style="position: absolute;clip: rect(0 0 0 0)"/>
                            <el-button type="primary" plain @click="$refs.pkFile.click()">{{
                                    $t('SelectFile')
                                }}
                            </el-button>
                            <el-button @click="sshInfo.password=''">{{ $t('Clear') }}</el-button>
                            <el-button type="primary" @click="textareaVisible = false; $emit('ssh-select')">{{
                                    $t('Connect')
                                }}
                            </el-button>
                        </div>
                    </el-dialog>
                    <el-form-item size="small">
                        <el-button type="primary" @click="$emit('ssh-select')" plain>{{ $t('Connect') }}</el-button>
                    </el-form-item>
                    <el-form-item size="small">
                        <!--file management -->
                        <file-list></file-list>
                    </el-form-item>
                    <el-form-item size="small">
                        <el-dropdown @command="handleCommand">
                            <el-button type="primary">
                                {{ $t('History') }}
                            </el-button>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item
                                    v-for="item in sshList"
                                    :key="item.host" :command="item" style="padding:0 5px 0 10px">
                                    {{ item.host }}
                                    <i @click="cleanHistory(item)" class="el-icon-close"></i>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </el-form-item>
                    <el-form-item size="small">
                        <el-button type="primary" v-show="showLogin" @click="foreShowLogin = true;" plain>{{
                                $t('login')
                            }}
                        </el-button>
                    </el-form-item>
                    <el-form-item size="small">
                        <el-button type="primary" v-show="showLogout" @click="handleLogout()" plain>{{
                                $t('logout')
                            }}
                        </el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <!-- login popup -->
        <el-dialog
            :title="$t('loginFormTitle')"
            :visible.sync="showLogin"
            width="30%">
            <el-form :model="login.form" ref="loginForm" :rules="login.checkRules">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="login.form.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="login.form.password" placeholder="请输入密码" type="password"></el-input>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="handleLogin" plain>{{ $t('login') }}</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import {getShouldVerifyToken} from '@/api/common'
import {login} from '@/api/common'
import {getLanguage} from '@/lang/index'
import FileList from '@/components/FileList'
import {mapState} from 'vuex'

export default {
    components: {
        'file-list': FileList
    },
    data() {
        return {
            foreShowLogin: false,
            loginLoading: false,
            textareaVisible: false,
            login: {
                form: {
                    username: 'test',
                    password: 'test@123!'
                },
                checkRules: {
                    username: [
                        {required: true, trigger: 'blur'}
                    ],
                    password: [
                        {required: true, trigger: 'blur', message: 'password is required'}
                    ]
                }
            },
            checkRules: {
                host: [
                    {required: true, trigger: 'blur'}
                ],
                port: [
                    {
                        required: true, trigger: 'blur', type: 'number', transform(value) {
                            return Number(value)
                        }
                    }
                ],
                username: [
                    {required: true, trigger: 'blur'}
                ],
                password: [
                    {required: true, trigger: 'blur', message: 'value is required'}
                ]
            }
        }
    },
    methods: {
        handleLogout() {
            const self = this;
            self.foreShowLogin = false;
            self.$emit('ssh-logout');
            self.$store.dispatch('setToken', '');
        },
        handleLogin() {
            const self = this;
            this.$refs.loginForm.validate((valid) => {
                if (valid && !self.loginLoading) {
                    self.loginLoading = true;
                    login(self.login.form).then(function (result) {
                        self.loginLoading = false;
                        if (result.code == '200') {
                            const token = result.Data.token;
                            if (token) {
                                self.$store.dispatch('setToken', token);
                            }
                        }

                    })
                }
            });
            setTimeout(function () {
                if (self.loginLoading) {
                    self.loginLoading = false;
                }
            }, 10);
        },
        handleSetLanguage() {
            const oldLang = getLanguage()
            const lang = oldLang === 'zh' ? 'en' : 'zh'
            this.$i18n.locale = lang
            this.$store.dispatch('setLanguage', lang)
        },
        handleCommand(command) {
            this.$store.commit('SET_SSH', command)
            if (command.password === undefined) {
                this.$store.commit('SET_PASS', '')
            }
            // 新开窗口
            this.$emit('ssh-select')
        },
        cleanHistory(command) {
            const sshListObj = this.sshList
            sshListObj.forEach((v, i) => {
                if (v.host === command.host) {
                    sshListObj.splice(i, 1)
                }
            })
            this.$store.commit('SET_LIST', window.btoa(JSON.stringify(sshListObj)))
        },
        handleChangePKFile(event) {
            const file = event.target.files[0]
            if (file) {
                const sshInfo = this.sshInfo
                const reader = new FileReader()
                reader.onload = e => {
                    sshInfo.password = e.target.result
                }
                reader.readAsText(file)
            }
        },
        getRequestParam(name) {
            console.log('request query:'+location.search);
            if (name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)')).exec(location.search))
                return decodeURIComponent(name[1]);
        }

    },
    mounted() {

        if (this.sshList.length > 0) {
            const latestSSH = this.sshList[this.sshList.length - 1]
            this.$store.commit('SET_SSH', latestSSH)
            if (latestSSH.password === undefined) {
                this.$store.commit('SET_PASS', '')
            }
        }
        const self = this;
        const contextPath = self.getRequestParam('prefix');
        console.log('contextPath:'+contextPath);
        if(contextPath){
            self.$store.state.ctx = contextPath;
        }

        getShouldVerifyToken().then(function (shouldVerifyToken) {
            self.$store.state.shouldValidToken = shouldVerifyToken.Data;
        });
    },
    computed: {
        ...mapState(['sshInfo']),
        privateKey() {
            return this.sshInfo.logintype === 1
        },
        showLogin() {
            const shouldValidToken = this.$store.state.shouldValidToken;
            const token = this.$store.state.token;
            if (shouldValidToken && (token == '' || token == null || token == undefined)) {
                return true;
            }
            if (this.foreShowLogin) {
                return true;
            }
            return false;
        },
        showLogout() {
            const shouldValidToken = this.$store.state.shouldValidToken;
            const token = this.$store.state.token;
            if (shouldValidToken && !(token == '' || token == null || token == undefined)) {
                return true;
            }
            return false;
        },
        sshList() {
            const sshList = this.$store.state.sshList
            if (sshList === null) {
                return []
            } else {
                return JSON.parse(window.atob(sshList))
            }
        }
    }
}
</script>

<style scoped>
</style>
