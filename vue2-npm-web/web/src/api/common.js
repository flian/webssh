import request from '@/utils/request'
export function checkSSH(sshInfo,token) {
    return request.get(`/webssh/check?sshInfo=${sshInfo}&token=${token}`)
}

export function getShouldVerifyToken(){
    return request.get("/webssh/shouldVerifyToken")
}

export function login(loginData){
    return request({
        url: '/webssh/login',
        method: 'post',
        data: loginData
    });
}
