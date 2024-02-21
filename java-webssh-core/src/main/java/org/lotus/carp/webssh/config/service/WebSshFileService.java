package org.lotus.carp.webssh.config.service;

import org.lotus.carp.webssh.config.controller.vo.FileListRequestParamsVo;
import org.lotus.carp.webssh.config.controller.vo.FileListVo;

/**
 * <h3>javaWebSSH</h3>
 * <p>web ssh file manager service</p>
 *
 * @author : foy
 * @date : 2024-02-21 16:59
 **/
public interface WebSshFileService {
    /**
     * list file info for upload/download
     * @param requestParamsVo request params
     * @return
     */
    FileListVo listFiles(FileListRequestParamsVo requestParamsVo);
}
