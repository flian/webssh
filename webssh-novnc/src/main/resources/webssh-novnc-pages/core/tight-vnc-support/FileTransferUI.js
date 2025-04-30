class FileTransferUIExtend {
// 在文件传输类中添加限制
    constructor(rfb, options = {}) {
        this.maxFileSize = options.maxFileSize || 100 * 1024 * 1024; // 100MB
    }

    uploadFile(file, remotePath) {
        if (file.size > this.maxFileSize) {
            return Promise.reject('File size exceeds limit');
        }
        // ...原有实现
    }
}


function integrateFileTransferUI(rfb) {
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

    // 初始化文件传输
    const ft = new TightVNCFileTransfer(rfb, {
        port: 5901 // TightVNC文件传输端口
    });

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

function validatePath(path) {
    // 防止目录遍历攻击
    return !path.includes('../') && !path.includes('..\\');
}

// 检测TightVNC文件传输支持
function checkFileTransferSupport(rfb) {
    return new Promise((resolve) => {
        const ft = new TightVNCFileTransfer(rfb);
        ft.socket.onopen = () => {
            setTimeout(() => {
                resolve(ft.socket.readyState === WebSocket.OPEN);
            }, 1000);
        };
        ft.socket.onerror = () => resolve(false);
    });
}

// 使用示例
checkFileTransferSupport(rfb).then(supported => {
    if (supported) {
        integrateFileTransferUI(rfb);
    } else {
        console.log('TightVNC file transfer not available');
    }
});