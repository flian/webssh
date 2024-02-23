package org.lotus.carp.webssh.config.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.vo.*;
import org.lotus.carp.webssh.config.service.WebSshFileService;
import org.lotus.carp.webssh.config.service.impl.vo.CachedWebSocketSessionObject;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-21 17:02
 **/
@Slf4j
public class DefaultJschWebSshFileServiceImpl extends JschBase implements WebSshFileService {

    private static final String[] SIZE_STR = {"Byte", "KB", "MB", "GB", "TB"};
    private static final long RATE = 1024;

    public String ensurePath(String path) {
        if (ObjectUtils.isEmpty(path)) {
            return "/root";
        }
        return path;
    }

    //transfer file size to readable format
    private String transFileSize(long size) {
        int i = 0;
        while (size >= RATE && i < SIZE_STR.length) {
            i++;
            size = size / RATE;
        }
        return size + SIZE_STR[i];
    }

    @Data
    private static class UploadVo {
        //should mkdir bfore upload file
        private boolean shouldMkDir = false;
        //path info for file upload
        private String pathArr;
        //file full path to upload
        private String fileFullArr;
    }


    @Override
    public FileUploadResultVo uploadFile(FileUploadDataVo fileUploadDataRequest, MultipartFile file) {
        FileUploadResultVo result = new FileUploadResultVo();
        result.setOk(true);
        String path = ensurePath(fileUploadDataRequest.getPath());
        String dir = fileUploadDataRequest.getDir();
        String pathArr = path;
        UploadVo uploadVo = new UploadVo();
        uploadVo.setPathArr(pathArr);
        if (!ObjectUtils.isEmpty(dir)) {
            pathArr = pathArr + dir;
            uploadVo.setPathArr(pathArr);
            uploadVo.shouldMkDir = true;
        }
        String fileName = file.getName();
        if (ObjectUtils.isEmpty(fileName)) {
            return FileUploadResultVo.failure("upload file name is not allow empty, please ensure file hase name.");
        }
        uploadVo.setFileFullArr(pathArr + fileName);

        ensureChannelSftpAndExec(fileUploadDataRequest.getSshInfo(), fileUploadDataRequest.getToken(), sftp -> {
            if (uploadVo.shouldMkDir) {
                try {
                    sftp.mkdir(uploadVo.pathArr);
                    //try to use resume model.
                    try {
                        SftpATTRS attrs = sftp.stat(uploadVo.fileFullArr);
                        log.info("file exist.. using RESUME mode to transfer file.");
                        sftp.put(file.getInputStream(), uploadVo.fileFullArr,
                                new JschSftpUploadProcessMonitor(fileUploadDataRequest.getId(), file.getSize()), ChannelSftp.RESUME);
                    } catch (SftpException e) {
                        log.info("file not exist.. using overwrite mode to transfer file.");
                        sftp.put(file.getInputStream(), uploadVo.fileFullArr,
                                new JschSftpUploadProcessMonitor(fileUploadDataRequest.getId(), file.getSize()), ChannelSftp.OVERWRITE);
                    }

                } catch (SftpException | IOException e) {
                    result.setOk(false);
                    result.setMsg("error while upload" + e.getMessage());
                }
            }
        });

        return result;
    }

    /**
     * download file from remote server
     *
     * @param downLoadRequest server and request information
     * @param outputStream    outputStream will write to
     */
    @Override
    public void downloadFile(FileDownLoadParamsVo downLoadRequest, OutputStream outputStream) {
        ensureChannelSftpAndExec(downLoadRequest.getSshInfo(), downLoadRequest.getToken(), sftp -> {
            try {
                sftp.get(downLoadRequest.getPath(), outputStream);
            } catch (SftpException e) {
                log.error("error while download file from remote server,path:{},error:{}", downLoadRequest.getPath(), e);
            }
        });
    }

    /**
     * list file info for upload/download
     *
     * @param requestParamsVo request params
     * @return
     */
    @Override
    public FileListVo listFiles(FileListRequestParamsVo requestParamsVo) {

        String path = ensurePath(requestParamsVo.getPath());
        FileListVo result = new FileListVo();
        result.setPath(path);

        List<FileMetaVo> fileList = new ArrayList<>();
        result.setList(fileList);
        try {
            ensureChannelSftpAndExec(requestParamsVo.getSshInfo(), requestParamsVo.getToken(), sftp -> {

                Vector<ChannelSftp.LsEntry> list = null;
                try {
                    sftp.cd(requestParamsVo.getPath());
                    list = sftp.ls(path);
                } catch (SftpException e) {
                    log.error("error while list remote server file list.", e);
                    result.setMsg("500 server error!" + e.getMessage());
                }
                if (!CollectionUtils.isEmpty(list)) {
                    list.stream().filter(f -> !(f.getFilename().equals(".") || f.getFilename().equals(".."))).forEach(f -> {
                        FileMetaVo fileMeta = new FileMetaVo();
                        fileMeta.setName(f.getFilename());
                        fileMeta.setDir(f.getAttrs().isDir());
                        fileMeta.setModifyTime(new Date(((long) f.getAttrs().getMTime()) * 1000L));

                        fileMeta.setPermissionsString(f.getAttrs().getPermissionsString());
                        fileMeta.setAddTime(new Date(((long) f.getAttrs().getATime()) * 1000L));
                        if (fileMeta.getIsDir()) {
                            fileMeta.setSize("" + f.getAttrs().getSize());
                        } else {
                            fileMeta.setSize(transFileSize(f.getAttrs().getSize()));
                        }
                        fileList.add(fileMeta);
                    });
                }
            });

        } catch (Exception e) {
            log.error("error while list files for remote server. path:{},e:{}", requestParamsVo.getPath(), e);
            result.setMsg("500 server error!" + e.getMessage());
        }
        return result;
    }
}
