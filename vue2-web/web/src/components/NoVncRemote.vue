<template>
    <div class="NoVncContainer">
        <el-button type="primary" size="small" @click="dialogVisible=true">{{$t('NoVncRemote')}}</el-button>
        <el-dialog :title="$t('NoVncRemote')" :visible.sync="dialogVisible" top="5vh" :width="dialogWidth">

            <el-row>
                <el-col><el-link type="success" @click="openNoVncPage()" target="_blank" :visible="vncLinkOk">{{$t('goVncPage')}}</el-link> </el-col>
            </el-row>
            <el-tabs v-model="tabActiveName">
                <el-tab-pane :label="$t('NoVncRemoteConfig')" name="noVncSettings">
                    <div style="margin: 20px;margin-top: 30px"></div>
                        <el-form label-position="right" size="small">
                            <el-form-item label="NoVncServerHost" size="small" prop="NoVncServerHost" >
                                <el-input v-model="noVncConfig.host" />
                            </el-form-item>
                            <br/>
                            <el-form-item label="NoVncServerPort" size="small" prop="NoVncServerPort" >
                                <el-input-number v-model="noVncConfig.port" />
                            </el-form-item>
                            <br/>
                            <el-form-item label="NoVncServerLinux" size="small" prop="NoVncServerPort">
                                <el-checkbox v-model="noVncConfig.linux" @change="checked=>isLinuxChange(checked,noVncConfig.linux)">
                                    Remote Server is Linux
                                    <el-tooltip placement="right">
                                        <div slot="content">{{$t('LinuxBtnDesc')}}</div>
                                        <i class="el-icon-question icon-color"></i>
                                    </el-tooltip>
                                </el-checkbox>
                            </el-form-item>
                            <br/>
                            <el-button type="primary" @click="updateVncServerInfos()">{{$t('updateVncInfo')}}</el-button>
                            <el-button type="primary" @click="openNoVncPage()" :disabled="serverInfoNeedUpdate">{{$t('goVncPage')}}</el-button>

                        </el-form>

                </el-tab-pane>

            </el-tabs>



        </el-dialog>
    </div>
</template>

<script>
import {getNoVncInfo} from '@/api/no_vnc_infos'
export default {
    name:'NoVncRemote',
    created() {
        //this.fetchInfos();
    },
    data(){
        return {
            serverInfoNeedUpdate: true,
            tabActiveName: 'noVncSettings',
            dialogVisible: false,
            dialogWidth: '70%',
            serverProxyInfos:{},
            vncLinkOk: false,
            currentNoVncInfo:{
                host: '',
                linux: false,
                nextPort: 5900,
                currentVncClientCnt: 0,
                vncHtmlUrl: '',
            },
            noVncConfig: {
                host: '',
                linux: false,
                port: 5900
            }
        }
    },
    computed:{
        goVncHtmlUrl(){
            return this.currentNoVncInfo.vncHtmlUrl;
        }
    },
    methods:{
        isLinuxChange(checked, model) {
            console.log(model);
            const self = this;
            if (checked) {
                //linux default
                self.noVncConfig.port = 5901;
            } else {
                //windows default
                self.noVncConfig.port = 5900;
            }
            self.serverInfoNeedUpdate = true;
        },
        updateVncServerInfos(){
            const self = this;
            getNoVncInfo(self.getCurrentToken()
                ,self.noVncConfig.host,self.noVncConfig.linux,true).then(function (result){
                if(result.code == '200'){
                    const info = result.data;
                    self.vncLinkOk = true;
                    self.serverInfoNeedUpdate = false;
                    self.currentNoVncInfo.host = info.host;
                    self.currentNoVncInfo.linux = info.linux;
                    self.currentNoVncInfo.nextPort = info.nextPort;
                    self.currentNoVncInfo.currentVncClientCnt = info.currentVncClientCnt;
                    self.currentNoVncInfo.vncHtmlUrl = info.vncHtmlUrl;
                    //update config based on server result.
                    self.noVncConfig.port = self.currentNoVncInfo.nextPort;
                    self.noVncConfig.linux = self.currentNoVncInfo.linux;
                }else {
                    self.vncLinkOk = false;
                    self.serverInfoNeedUpdate = true;
                }

            });
        },
        getCurrentToken(){
            return this.$store.getters.token;
        },
        openNoVncPage(){
            const self = this;
            const prefix = process.env.NODE_ENV === 'production' ? `${location.origin}` : 'api'
            window.parent.open(`${prefix}${self.goVncHtmlUrl}`,'_target=_blank')
        },

    }
}
</script>
<style scoped lang="scss">
.NoVncContainer {
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
