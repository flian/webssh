class TightVNCFileTransfer {
    constructor(rfb, options = {}) {
        this.rfb = rfb;
        this.host = options.host;
        this.token = options.token;
        this.port = options.port || 5901; // TightVNC默认文件传输端口
        this.maxFileSize = options.maxFileSize || 100 * 1024 * 1024; // 100MB
        this.socket = null;
        this.callbacks = {};
        this.initFileTransfer();
    }

    initFileTransfer() {
        //FIXME need refactor
        const wsUrl = new URL(`wss://${window.location.hostname}:${window.location.port}/websockify/ftproxy`);
        const queryParams = new URLSearchParams(location.search);
        url.searchParams.set('token',this.token);
        url.searchParams.set('noVncTargetHost',this.host);
        url.searchParams.set('noVncTargetPort', this.port );


        // 建立独立的WebSocket连接

        this.socket = new WebSocket(wsUrl);

        this.socket.binaryType = 'arraybuffer';

        this.socket.onopen = () => {
            console.log('File transfer channel connected');
            this._sendHandshake();
        };

        this.socket.onmessage = (e) => {
            this._handleServerMessage(new Uint8Array(e.data));
        };
    }

    _sendHandshake() {
        const handshake = new TextEncoder().encode("FileTransfer");
        this._sendMessage(handshake);
    }

    _handleServerMessage(data) {
        const msgType = data[0];

        switch (msgType) {
            case 0x01: // 服务器能力
                this._handleCapabilities(data);
                break;
            case 0x03: // 文件列表
                this._handleFileList(data);
                break;
            case 0x05: // 文件数据
                this._handleFileData(data);
                break;
            // ...其他消息类型处理
        }
    }

    _sendMessage(data) {
        if (this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(data);
        } else {
            console.error('File transfer channel not ready');
        }
    }

    validatePath(path) {
        // 防止目录遍历攻击
        return !path.includes('../') && !path.includes('..\\');
    }

    listDirectory(path = "") {
        if(!this.validatePath(path)){
            return Promise.reject('File path is not valid.');
        }

        return new Promise((resolve) => {
            const callbackId = this._generateCallbackId();
            this.callbacks[callbackId] = resolve;

            const pathEncoded = new TextEncoder().encode(path);
            const buffer = new ArrayBuffer(3 + pathEncoded.length);
            const view = new DataView(buffer);

            view.setUint8(0, 0x02); // 文件列表请求
            view.setUint16(1, 0x0001); // 标志位(显示隐藏文件)
            view.setUint16(3, pathEncoded.length);

            const message = new Uint8Array(buffer);
            message.set(pathEncoded, 5);

            this._sendMessage(message);
        });
    }

    _handleFileList(data) {
        const count = new DataView(data.buffer).getUint16(1);
        let pos = 3;
        const files = [];

        for (let i = 0; i < count; i++) {
            const nameLen = new DataView(data.buffer).getUint16(pos);
            pos += 2;

            const name = new TextDecoder().decode(data.subarray(pos, pos + nameLen));
            pos += nameLen;

            const size = new DataView(data.buffer).getBigUint64(pos);
            pos += 8;

            const mtime = new DataView(data.buffer).getUint32(pos);
            pos += 4;

            const attrs = new DataView(data.buffer).getUint16(pos);
            pos += 2;

            files.push({
                name,
                size,
                mtime: new Date(mtime * 1000),
                isDirectory: !!(attrs & 0x01),
                isHidden: !!(attrs & 0x02)
            });
        }

        // 触发回调
        if (this.callbacks[callbackId]) {
            this.callbacks[callbackId](files);
            delete this.callbacks[callbackId];
        }
    }

    downloadFile(remotePath, localName = null) {
        return new Promise((resolve, reject) => {
            const callbackId = this._generateCallbackId();
            this.callbacks[callbackId] = {resolve, reject, chunks: []};

            const pathEncoded = new TextEncoder().encode(remotePath);
            const buffer = new ArrayBuffer(3 + pathEncoded.length);
            const view = new DataView(buffer);

            view.setUint8(0, 0x04); // 下载请求
            view.setUint16(1, 0x0000); // 标志位

            const message = new Uint8Array(buffer);
            message.set(pathEncoded, 3);

            this._sendMessage(message);
        });
    }

    _handleFileData(data) {
        const flags = new DataView(data.buffer).getUint16(1);
        const length = new DataView(data.buffer).getUint32(3);
        const chunkData = data.subarray(7, 7 + length);

        // 保存数据块
        this.currentDownload.chunks.push(chunkData);

        if (flags & 0x0001) { // 最后一个数据包
            const blob = new Blob(this.currentDownload.chunks);
            const url = URL.createObjectURL(blob);

            const a = document.createElement('a');
            a.href = url;
            a.download = this.currentDownload.filename || 'download';
            a.click();

            URL.revokeObjectURL(url);
            this.currentDownload.resolve();
            this.currentDownload = null;
        }
    }

    uploadFile(localFile, remotePath) {
        if (localFile.size > this.maxFileSize) {
            return Promise.reject('File size exceeds limit');
        }
        return new Promise((resolve, reject) => {
            const callbackId = this._generateCallbackId();
            this.callbacks[callbackId] = {resolve, reject};

            // 发送上传请求
            const pathEncoded = new TextEncoder().encode(remotePath);
            const buffer = new ArrayBuffer(11 + pathEncoded.length);
            const view = new DataView(buffer);

            view.setUint8(0, 0x06); // 上传开始
            view.setUint16(1, 0x0000); // 标志位
            view.setBigUint64(3, BigInt(localFile.size));
            view.setUint16(11, pathEncoded.length);

            const message = new Uint8Array(buffer);
            message.set(pathEncoded, 13);

            this._sendMessage(message);

            // 分块发送文件数据
            this._uploadFileChunks(localFile);
        });
    }

    _uploadFileChunks(file) {
        const chunkSize = 32 * 1024; // 32KB chunks
        let offset = 0;
        const reader = new FileReader();

        reader.onload = (e) => {
            const buffer = new ArrayBuffer(5 + e.target.result.byteLength);
            const view = new DataView(buffer);

            view.setUint8(0, 0x07); // 上传数据
            view.setUint32(1, e.target.result.byteLength);

            const message = new Uint8Array(buffer);
            message.set(new Uint8Array(e.target.result), 5);

            this._sendMessage(message);

            offset += e.target.result.byteLength;
            if (offset < file.size) {
                this._readNextChunk(file, reader, offset, chunkSize);
            }
        };

        this._readNextChunk(file, reader, 0, chunkSize);
    }

    _readNextChunk(file, reader, offset, chunkSize) {
        const end = Math.min(offset + chunkSize, file.size);
        reader.readAsArrayBuffer(file.slice(offset, end));
    }
}