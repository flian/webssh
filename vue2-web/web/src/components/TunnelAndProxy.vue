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
                        <el-input v-model="http.bindPort" />
                    </el-form-item>
                    <el-form-item label="Username" size="small" prop="Username" >
                        <el-input v-model="http.username" />
                    </el-form-item>
                    <el-form-item label="Password" size="small" prop="Password" >
                        <el-input v-model="http.password" />
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
                        <el-input v-model="socket.bindPort" />
                    </el-form-item>
                    <el-form-item label="Username" size="small" prop="Username" >
                        <el-input v-model="socket.username" />
                    </el-form-item>
                    <el-form-item label="Password" size="small" prop="Password" >
                        <el-input v-model="socket.password" />
                    </el-form-item>
                    <el-button>save config and restart</el-button><el-button>Start</el-button><el-button>Stop</el-button>
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
    data(){
        return {
            dialogVisible: false,
            dialogWidth: '70%',
            http: {
                proxyType: '1',
                bindIp: '0.0.0.0',
                bindPort: '9966',
                username: '',
                password: '',
                running: false
            },
            socket: {
                proxyType: '0',
                bindIp: '0.0.0.0',
                bindPort: '9688',
                username: '',
                password: '',
                running: false
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
        getCurrentToken(){
            return this.$store.getters.token;
        },
        updateProxyInfo(){
            let token = this.$store.getters.token;
            const infoResult =  getTunnelAndProxyInfo(token);
            let info = {};
            const updateProxyResult = updateProxy(info,token);
        }
    }
}
</script>
<style scoped lang="scss">

</style>
