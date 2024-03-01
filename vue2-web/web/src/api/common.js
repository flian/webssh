import request from '@/utils/request'
export function checkSSH(sshInfo,token) {
    return request.get(`/webssh/check?sshInfo=${sshInfo}&token=${token}`)
}

export function getShouldVerifyToken(){
    return request.get('/webssh/shouldVerifyToken')
}

export function getProjectHeaders(token){
    return request.get(`/webssh/projectHeader/params?token=${token}`)
}

export function login(loginData){
    return request({
        url: '/webssh/login',
        method: 'post',
        data: loginData
    })
}
