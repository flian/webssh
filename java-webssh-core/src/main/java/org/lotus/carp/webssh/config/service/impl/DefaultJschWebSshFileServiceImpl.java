package org.lotus.carp.webssh.config.service.impl;

import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.vo.FileListRequestParamsVo;
import org.lotus.carp.webssh.config.controller.vo.FileListVo;
import org.lotus.carp.webssh.config.controller.vo.FileMetaVo;
import org.lotus.carp.webssh.config.service.WebSshFileService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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

    public String ensurePath(String path){
        if(ObjectUtils.isEmpty(path)){
            return "/root";
        }
        return path;
    }

    @Override
    public FileListVo listFiles(FileListRequestParamsVo requestParamsVo) {

        String path = ensurePath(requestParamsVo.getPath());
        FileListVo result = new FileListVo();
        result.setPath(path);

        List<FileMetaVo> fileList = new ArrayList<>();
        result.setList(fileList);
        try {
            ChannelSftp sftp = createChannelSftp(requestParamsVo.getSshInfo());
            sftp.cd(requestParamsVo.getPath());
            Vector<ChannelSftp.LsEntry> list = sftp.ls(path);
            if (!CollectionUtils.isEmpty(list)) {
                list.stream().forEach(f -> {
                    FileMetaVo fileMeta = new FileMetaVo();
                    fileMeta.setName(f.getFilename());
                    fileMeta.setDir(f.getAttrs().isDir());
                    fileMeta.setModifyTime(new Date(((long) f.getAttrs().getMTime()) * 1000L));
                    fileMeta.setSize("" + f.getAttrs().getSize());
                    fileMeta.setPermissionsString(f.getAttrs().getPermissionsString());
                    fileMeta.setAddTime(new Date(((long) f.getAttrs().getATime()) * 1000L));
                    fileList.add(fileMeta);
                });
            }
        } catch (Exception e) {
            log.error("error while list files for remote server. path:{},e:{}", requestParamsVo.getPath(), e);
        }
        return result;
    }
}
