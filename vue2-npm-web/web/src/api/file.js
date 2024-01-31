import request from '@/utils/request'
export function fileList(path, sshInfo,token) {
    return request.get(`/webssh/file/list?path=${path}&sshInfo=${sshInfo}&token=${token}`)
}
