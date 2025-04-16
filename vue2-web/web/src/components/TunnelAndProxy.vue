<template>
    <div class="MainContainer">
        <el-button type="primary" size="small" @click="dialogVisible = true">{{$t('TunnelAndProxy')}}</el-button>
        <el-dialog :title="$t('TunnelAndProxy')" :visible.sync="dialogVisible" top="5vh" :width="dialogWidth">
            <el-row>
                <el-col>mysql:aaa</el-col>
            </el-row>
            <el-row>
                <el-col>pg sql:aaa</el-col>
            </el-row>
            <el-row>
                <el-col>sqlite:aaa</el-col>
            </el-row>
            <el-row>
                <el-col>php info</el-col>
            </el-row>
            <el-row>
                <el-col>http proxy</el-col>
            </el-row>
            <el-row>
                <el-col>socket proxy</el-col>
            </el-row>
        </el-dialog>
    </div>
</template>

<script>

export default {
    name:"TunnelAndProxy",
    data(){
        return {
            dialogVisible: false,
            dialogWidth: '70%',
        }
    }
}
</script>
<style scoped lang="scss">

</style>
