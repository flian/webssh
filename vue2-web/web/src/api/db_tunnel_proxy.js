import request from '@/utils/request'

export function getTunnelAndProxyInfo(token){
    return request.get(`/webssh/tunnelAndProxy/info?token=${token}`,{timeout:30000});
}

export function  updateProxy(proxyInfo,token){
    return request({
        url:`/webssh/tunnelAndProxy/updateProxy?token=${token}`,
        method: 'post',
        data: proxyInfo,
        config:{timeout:30000}
    });
}
