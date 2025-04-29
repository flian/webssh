export default {
    sshReq: state => {
        const rdpConfig= JSON.stringify(state.sshInfo.rdpConfig)
        return window.btoa(
            `{
            "username":"${state.sshInfo.username}",
            "ipaddress":"${state.sshInfo.host}",
            "port":${state.sshInfo.port},
            "password":"${state.sshInfo.password.replace(/[\n]/g, '\\n')}",
            "logintype":${state.sshInfo.logintype === undefined ? 0 : state.sshInfo.logintype},
            "rdpConfig": ${rdpConfig}
        }`)
    },
    token: state => state.token,
    ctx: state => state.ctx,
    projectHeaderParams: state => state.projectHeaderParams,
}
