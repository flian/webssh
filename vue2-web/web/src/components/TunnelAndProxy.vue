<template>
    <div class="TunnelAndProxyContainer">
        <el-button type="primary" size="small" @click="fetchInfos();dialogVisible=true">{{$t('TunnelAndProxy')}}</el-button>
        <el-dialog :title="$t('TunnelAndProxy')" :visible.sync="dialogVisible" top="5vh" :width="dialogWidth">

            <el-row>
                <el-col><el-link type="success" @click="openPhpInfoPage" target="_blank">{{$t('phpInfo')}}</el-link> </el-col>
            </el-row>
            <el-tabs v-model="tabActiveName">
                <el-tab-pane :label="$t('tunnelPanTitle')" name="tunnel">

                    <el-row>
                        <el-col>mysql:<el-input v-model="mysqlTunnelUrl" :disabled="true"></el-input><el-button type="primary" v-clipboard:copy="copyFullUrl('mysql')">{{$t('Copy')}}</el-button></el-col>
                    </el-row>
                    <el-row>
                        <el-col>pgsql:<el-input v-model="pgsqlTunnelUrl" :disabled="true"></el-input><el-button type="primary" v-clipboard:copy="copyFullUrl('pgsql')">{{$t('Copy')}}</el-button></el-col>
                    </el-row>
                    <el-row>
                        <el-col>sqlite:<el-input v-model="sqliteTunnelUrl" :disabled="true"></el-input><el-button type="primary" v-clipboard:copy="copyFullUrl('sqlite')">{{$t('Copy')}}</el-button></el-col>
                    </el-row>
                </el-tab-pane>

                <el-tab-pane :label="$t('socketProxyPanTitle')" name="socketProxy">
                    <div style="margin: 20px;margin-top: 30px"></div>
                        <el-form label-position="right" size="small">
                            <el-form-item label="BindIp" size="small" prop="BindIp" >
                                <el-input v-model="socket.bindIp" />
                            </el-form-item>
                            <el-form-item label="BindPort" size="small" prop="BindPort" >
                                <el-input-number v-model="socket.bindPort" />
                            </el-form-item>
                            <br/>
                            <el-form-item label="Username" size="small" prop="Username" >
                                <el-input v-model="socket.username" />
                            </el-form-item>
                            <el-form-item label="Password" size="small" prop="Password" >
                                <el-input v-model="socket.password" show-password/>
                            </el-form-item>
                            <br/>
                            <el-form-item label="AutoStopIn" size="small" prop="AutoStopIn" >
                                <el-input-number v-model="socket.autoStopIn" :disabled="true"/>
                                <el-tooltip placement="right">
                                    <div slot="content">{{$t('AutoStopInDesc')}}</div>
                                    <i class="el-icon-question icon-color"></i>
                                </el-tooltip>
                            </el-form-item>

                            <el-tag type="success" v-if="socket.running">{{$t('running')}}</el-tag>
                            <el-tag type="warning" v-if="!socket.running">{{$t('stopped')}}</el-tag>
                            <br/>
                            <el-button type="primary" @click="updateSocketProxy(-1)">{{$t('saveAndRestartProxy')}}</el-button>
                            <el-button type="primary" @click="updateSocketProxy(0)">{{$t('startProxy')}}</el-button>
                            <el-button type="danger" @click="updateSocketProxy(1)">{{$t('stopProxy')}}</el-button>
                        </el-form>

                </el-tab-pane>
                <el-tab-pane :label="$t('httpProxyPanTitle')" name="httpProxy">
                    <div style="margin: 20px;margin-top: 30px"></div>
                    <el-form label-position="right" size="small">
                            <el-form-item label="BindIp" size="small" prop="BindIp" >
                                <el-input v-model="http.bindIp" />
                            </el-form-item>
                            <el-form-item label="BindPort" size="small" prop="BindPort" >
                                <el-input-number v-model="http.bindPort" />
                            </el-form-item>
                            <br/>
                            <el-form-item label="Username" size="small" prop="Username" >
                                <el-input v-model="http.username" />
                            </el-form-item>
                            <el-form-item label="Password" size="small" prop="Password" >
                                <el-input v-model="http.password" show-password/>
                            </el-form-item>
                            <br/>
                            <el-form-item label="AutoStopIn" size="small" prop="AutoStopIn" >
                                <el-input-number v-model="http.autoStopIn" :disabled="true">
                                </el-input-number>
                                <el-tooltip placement="right">
                                    <div slot="content">{{$t('AutoStopInDesc')}}</div>
                                    <i class="el-icon-question icon-color"></i>
                                </el-tooltip>
                            </el-form-item>
                            <el-tag type="success" v-if="http.running">{{$t('running')}}</el-tag>
                            <el-tag type="warning" v-if="!http.running">{{$t('stopped')}}</el-tag>
                            <br/>
                            <el-row>
                            <el-form-item label="HttpProxyUrl" size="small" prop="HttpProxyUrl">
                                <el-input v-model="http.httpProxyUrl" :disabled="true"/>
                            </el-form-item>
                                <el-button type="primary" v-clipboard:copy="copyHttpProxyUrl()">{{$t('Copy')}}</el-button>
                            </el-row>
                            <br/>
                            <el-button type="primary" @click="updateHttpProxy(-1)">{{$t('saveAndRestartProxy')}}</el-button>
                            <el-button type="primary" @click="updateHttpProxy(0)">{{$t('startProxy')}}</el-button>
                            <el-button type="danger" @click="updateHttpProxy(1)">{{$t('stopProxy')}}</el-button>
                    </el-form>

                </el-tab-pane>
            </el-tabs>



        </el-dialog>
    </div>
</template>

<script>
import {getTunnelAndProxyInfo, updateProxy} from '@/api/db_tunnel_proxy'

import {Message} from 'element-ui'

export default {
    name:'TunnelAndProxy',
    created() {
        //this.fetchInfos();
    },
    data(){
        return {
            tabActiveName: 'tunnel',
            dialogVisible: false,
            dialogWidth: '70%',
            serverProxyInfos:{},
            http: {
                proxyType: '1',
                op: '-1',
                bindIp: '0.0.0.0',
                bindPort: '9966',
                username: '',
                password: '',
                running: false,
                httpProxyUrl: '',
                autoStopIn: '6'
            },
            socket: {
                proxyType: '0',
                op: '-1',
                bindIp: '0.0.0.0',
                bindPort: '9688',
                username: '',
                password: '',
                running: false,
                autoStopIn: '6'
            }
        }
    },
    computed:{
        phpInfoUrl(){
            return '/php/navicat/ntunnel/php_info?token='+this.getCurrentToken();
        },
        mysqlTunnelUrl(){
            return '/php/navicat/ntunnel/mysql?token='+this.getCurrentToken();
        },
        pgsqlTunnelUrl(){
            return '/php/navicat/ntunnel/pgsql?token='+this.getCurrentToken();
        },
        sqliteTunnelUrl(){
            return '/php/navicat/ntunnel/sqlite?token='+this.getCurrentToken();
        },

    },
    methods:{
        copyFullUrl(dbName){
            const self = this;
            //default is mysql
            let suffix =self.mysqlTunnelUrl;
            if(dbName == 'pgsql'){
                suffix =self.pgsqlTunnelUrl;
            }
            if(dbName == 'sqlite'){
                suffix =self.sqliteTunnelUrl;
            }
            const prefix = `${self.serverProxyInfos.schema}://${self.serverProxyInfos.host}:${self.serverProxyInfos.port}`;
            return prefix+suffix;
        },
        copyHttpProxyUrl(){
            const self = this;
            return self.http.httpProxyUrl;
        },
        fetchInfos(){
            const self = this;
            getTunnelAndProxyInfo(self.getCurrentToken()).then(function (result){
                if(result.code == '200'){
                    self.serverProxyInfos = result.data;
                    self.serverProxyInfos.items.forEach((proxyItem)=>{
                        if(proxyItem.proxyType == 0){
                            //socket
                            self.socket.bindIp = proxyItem.host;
                            self.socket.bindPort = proxyItem.port;
                            self.socket.username = proxyItem.username;
                            self.socket.password = proxyItem.password;
                            self.socket.running = proxyItem.running;
                        }
                        if(proxyItem.proxyType == 1){
                            //http
                            self.http.bindIp = proxyItem.host;
                            self.http.bindPort = proxyItem.port;
                            self.http.httpProxyUrl = proxyItem.httpProxyUrl;
                            self.http.username = proxyItem.username;
                            self.http.password = proxyItem.password;
                            self.http.running = proxyItem.running;
                        }
                    });
                }
            });
        },
        getCurrentToken(){
            return this.$store.getters.token;
        },
        updateHttpProxy(op){
            const self = this;
            self.http.op = op;
            updateProxy(self.http,self.getCurrentToken()).then(result=>{
                if(result.code == '200'){
                    Message.success('process success.');
                    self.fetchInfos();
                }
            });

        },
        openPhpInfoPage(){
            const self = this;
            const prefix = process.env.NODE_ENV === 'production' ? `${location.origin}` : 'api'
            window.parent.open(`${prefix}${self.phpInfoUrl}`,'_target=_blank')
        },
        updateSocketProxy(op){
            const self = this;
            self.socket.op = op;
            updateProxy(self.socket,self.getCurrentToken()).then(result=>{
                if(result.code == '200'){
                    Message.success('process success.');
                    self.fetchInfos();
                }
            });
        },
    }
}
</script>
<style scoped lang="scss">
.TunnelAndProxyContainer {
    .el-dialog__wrapper {
        overflow: hidden;
    }
    .el-input__inner {
        border: 0 none;
        border-bottom: 1px solid #ccc;
        border-radius: 0px;
        width: 100%;
    }
    .el-table--border tr,td{
        border: none!important;
    }
    .el-table::before{
        height:0;
    }
    .el-table td, .el-table th {
        padding: 2px 0;
    }
    .el-dropdown {
        display: flex;
    }
}

</style>
