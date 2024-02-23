package org.lotus.carp.webssh.config.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.vo.FileDownLoadParamsVo;
import org.lotus.carp.webssh.config.controller.vo.FileListRequestParamsVo;
import org.lotus.carp.webssh.config.controller.vo.FileListVo;
import org.lotus.carp.webssh.config.controller.vo.FileMetaVo;
import org.lotus.carp.webssh.config.service.WebSshFileService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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

    /**
     * download file from remote server
     * @param downLoadRequest server and request information
     * @param outputStream outputStream will write to
     */
    @Override
    public void downloadFile(FileDownLoadParamsVo downLoadRequest, OutputStream outputStream) {
        ensureCreateChannelSftpAndExec(downLoadRequest.getSshInfo(), sftp -> {
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
            ensureCreateChannelSftpAndExec(requestParamsVo.getSshInfo(), sftp -> {

                Vector<ChannelSftp.LsEntry> list = null;
                try {
                    sftp.cd(requestParamsVo.getPath());
                    list = sftp.ls(path);
                } catch (SftpException e) {
                    log.error("error while list remote server file list.", e);
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
        } finally {

        }
        return result;
    }
}
