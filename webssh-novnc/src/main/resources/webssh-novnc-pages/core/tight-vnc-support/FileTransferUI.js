import {TightVNCFileTransfer} from "./TightVNCFileTransfer.js";

function integrateFileTransferUI(rfb,ft) {
    // 创建UI容器
    const ftContainer = document.createElement('div');
    ftContainer.className = 'noVNC-file-transfer';
    ftContainer.innerHTML = `
    <div class="file-list"></div>
    <input type="file" id="ft-upload" style="display:none">
    <button class="ft-btn ft-upload">Upload</button>
    <button class="ft-btn ft-download">Download</button>
    <button class="ft-btn ft-refresh">Refresh</button>
  `;

    // 添加到noVNC界面
    document.querySelector('.noVNC-container').appendChild(ftContainer);

    // 刷新文件列表
    const refreshList = () => {
        ft.listDirectory().then(files => {
            const listEl = ftContainer.querySelector('.file-list');
            listEl.innerHTML = files.map(file => `
        <div class="file-item ${file.isDirectory ? 'is-folder' : ''}">
          <span class="file-name">${file.name}</span>
          <span class="file-size">${file.isDirectory ? '' : formatSize(file.size)}</span>
        </div>
      `).join('');
        });
    };

    // 绑定按钮事件
    ftContainer.querySelector('.ft-refresh').addEventListener('click', refreshList);

    ftContainer.querySelector('.ft-upload').addEventListener('click', () => {
        document.getElementById('ft-upload').click();
    });

    document.getElementById('ft-upload').addEventListener('change', (e) => {
        if (e.target.files.length > 0) {
            ft.uploadFile(e.target.files[0], e.target.files[0].name)
                .then(() => refreshList());
        }
    });

    // 初始加载
    refreshList();
}

// 格式化文件大小
function formatSize(bytes) {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
}



// 检测TightVNC文件传输支持
function checkFileTransferSupport(rfb,options) {
    return new Promise((resolve) => {
        const ft = new TightVNCFileTransfer(rfb,options);
        ft.socket.onopen = () => {
            setTimeout(() => {
                resolve(ft.socket.readyState === WebSocket.OPEN);
            }, 1000);
        };
        ft.socket.onerror = () => resolve(false);
        return ft;
    });
}


//使用
export function letUsAddFileTransferUI(rfb){
    const queryParams = new URLSearchParams(location.search);
    const options = {
        host: queryParams.get('targetHost'),
        port: 5900, // TightVNC文件传输端口. queryParams.get('targetPort')
        token: queryParams.get('token')
    };
    // 初始化文件传输
    checkFileTransferSupport(rfb,options).then(tft => {
        if (tft) {
            integrateFileTransferUI(rfb,tft);
        } else {
            console.log('TightVNC file transfer not available');
        }
    });
}

