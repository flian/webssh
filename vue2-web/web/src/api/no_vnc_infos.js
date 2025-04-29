import request from '@/utils/request'

export function getNoVncInfo(token,host,linux,genVncIndexUrl){
    return request.get(`/novnc/host/info?token=${token}&host=${host}&linux=${linux}&genVncIndexUrl=${genVncIndexUrl}`,{timeout:30000});
}
