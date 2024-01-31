import request from '@/utils/request'
export function checkSSH(sshInfo,token) {
    return request.get(`/check?sshInfo=${sshInfo}&token=${token}`)
}

export function login(loginData){
    return request({
        url: '/webssh/login',
        method: 'post',
        data: data
    });
}
