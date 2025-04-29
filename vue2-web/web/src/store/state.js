import { getLanguage } from '@/lang/index'

export default {
    sshInfo: {
        host: '192.168.2.100',
        username: 'root',
        port: 22,
        password: '',
        logintype: 0,
        rdpConfig: {
            rdp: false,
            rdpServer: false,
            autoConnect: false,
            x11Display: '',
            windowsIp: '',
            rdpPort: '',
            rdpWindowsFullScreen: false,
            rpdWindowsSize: '',
            logLevel: 'INFO',
            rdpDiskDeviceMap: '',
            properJavaRDPDownloadUrl: ''
        }
    },
    sshList: Object.prototype.hasOwnProperty.call(localStorage, 'sshList') ? localStorage.getItem('sshList') : null,
    termList: [],
    currentTab: {},
    //should verify token
    shouldValidToken: true,
    //default rdp config
    defaultRdpConfig:{},
    token: '',
    projectExchangeToken: undefined,
    projectHeaderParams: [],
    ctx: undefined,
    language: getLanguage()
}
