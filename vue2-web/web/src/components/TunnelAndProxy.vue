<template>
    <div class="MainContainer">
        <el-button type="primary" size="small" @click="dialogVisible = true">{{$t('TunnelAndProxy')}}</el-button>
        <el-dialog :title="$t('TunnelAndProxy')" :visible.sync="dialogVisible" top="5vh" :width="dialogWidth">

            <el-row><el-col>{{$t('tunnelGroup')}}</el-col><el-col><el-link type="info" :link="phpInfoUrl">php info(quercus)</el-link> </el-col></el-row>
            <el-row>
                <el-col>mysql:<el-input v-model="mysqlTunnelUrl" :disabled="true"></el-input><el-button type="primary" v-clipboard:copy="mysqlTunnelUrl">{{$t('Copy')}}</el-button></el-col>
            </el-row>
            <el-row>
                <el-col>pgsql:<el-input v-model="pgsqlTunnelUrl" :disabled="true"></el-input><el-button type="primary" v-clipboard:copy="pgsqlTunnelUrl">{{$t('Copy')}}</el-button></el-col>
            </el-row>
            <el-row>
                <el-col>sqlite:<el-input v-model="sqliteTunnelUrl" :disabled="true"></el-input><el-button type="primary" v-clipboard:copy="sqliteTunnelUrl">{{$t('Copy')}}</el-button></el-col>
            </el-row>

            <el-row><el-col>{{$t('proxyGroup')}}</el-col></el-row>
            <el-row>
                <el-col>http proxy:
                    <el-form-item label="BindIp" size="small" prop="BindIp" >
                        <el-input v-model="http.bindIp" />
                    </el-form-item>
                    <el-form-item label="BindPort" size="small" prop="BindPort" >
                        <el-input-number v-model="http.bindPort" />
                    </el-form-item>
                    <el-form-item label="Username" size="small" prop="Username" >
                        <el-input v-model="http.username" />
                    </el-form-item>
                    <el-form-item label="Password" size="small" prop="Password" >
                        <el-input v-model="http.password" />
                    </el-form-item>
                    <el-form-item label="AutoStopIn" size="small" prop="AutoStopIn" >
                        <el-input-number v-model="http.autoStopIn" />
                    </el-form-item>
                    <el-button>save config and restart</el-button><el-button>Start</el-button><el-button>Stop</el-button>
                </el-col>
            </el-row>
            <el-row>
                <el-col>socket proxy:
                    <el-form-item label="BindIp" size="small" prop="BindIp" >
                        <el-input v-model="socket.bindIp" />
                    </el-form-item>
                    <el-form-item label="BindPort" size="small" prop="BindPort" >
                        <el-input-number v-model="socket.bindPort" />
                    </el-form-item>
                    <el-form-item label="Username" size="small" prop="Username" >
                        <el-input v-model="socket.username" />
                    </el-form-item>
                    <el-form-item label="Password" size="small" prop="Password" >
                        <el-input v-model="socket.password" />
                    </el-form-item>
                    <el-form-item label="AutoStopIn" size="small" prop="AutoStopIn" >
                        <el-input-number v-model="http.autoStopIn" :disabled="true"/>
                    </el-form-item>
                    <el-tag type="success" v-if="socket.running">{{$t('running')}}</el-tag>
                    <el-tag type="warning" v-if="!socket.running">{{$t('stopped')}}</el-tag>
                    <el-button type="primary">{{$t('saveAndRestartProxy')}}</el-button>
                    <el-button type="primary">{{$t('startProxy')}}</el-button>
                    <el-button type="danger">{{$t('stopProxy')}}</el-button>
                </el-col>

            </el-row>
        </el-dialog>
    </div>
</template>

<script>
import {getTunnelAndProxyInfo, updateProxy} from '@/api/db_tunnel_proxy'
import { mapState } from 'vuex'
import store from '@/store';
import {getCurrentInstance} from "vue";

export default {
    name:"TunnelAndProxy",
    created() {
        this.fetchInfos();
    },
    data(){
        return {
            dialogVisible: false,
            dialogWidth: '70%',
            serverProxyInfos:{},
            http: {
                proxyType: '1',
                op: '-1',
                bindIp: '0.0.0.0',
                bindPort: '9966',
                proxyUrl: '',
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
        }
    },
    methods:{
        fetchInfos(){
            const self = this;
            getTunnelAndProxyInfo(self.getCurrentToken()).then(function (result){
                if(result.code === '200'){
                    self.serverProxyInfos = result.data;
                    for(let proxyItem in self.serverProxyInfos.items){
                        if(proxyItem.proxyType === 0){
                            //socket
                            self.socket.bindIp = proxyItem.host;
                            self.socket.bindPort = proxyItem.port;
                            self.socket.username = proxyItem.username;
                            self.socket.password = proxyItem.password;
                            self.socket.running = proxyItem.running;
                        }
                        if(item.proxyType === 1){
                            //http
                            self.http.bindIp = proxyItem.host;
                            self.http.bindPort = proxyItem.port;
                            self.http.proxyUrl = proxyItem.httpProxyUrl;
                            self.http.username = proxyItem.username;
                            self.http.password = proxyItem.password;
                            self.http.running = proxyItem.running;
                        }
                    }
                }
            });
        },
        getCurrentToken(){
            return this.$store.getters.token;
        },
        updateHttpProxy(op){
            const self = this;
            self.http.op = op;
            updateProxy(self.http,self.getCurrentToken());
            self.fetchInfos();
        },
        updateSocketProxy(op){
            const self = this;
            self.socket.op = op;
            updateProxy(self.socket,self.getCurrentToken());
            self.fetchInfos();
        },
    }
}
</script>
<style scoped lang="scss">

</style>
