package org.lotus.carp.webssh.config.service;

import org.lotus.carp.webssh.config.controller.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * <h3>javaWebSSH</h3>
 * <p>web ssh file manager service</p>
 *
 * @author : foy
 * @date : 2024-02-21 16:59
 **/
public interface WebSshFileService {


    /**
     * process file upload
     * @param fileUploadDataRequest file upload ssh info
     * @param file file to upload
     * @return
     */
    FileUploadResultVo uploadFile(FileUploadDataVo fileUploadDataRequest, MultipartFile file);

    /**
     * download file from remote server
     * @param downLoadRequest server and request information
     * @param outputStream outputStream will write to
     */
    void downloadFile(FileDownLoadParamsVo downLoadRequest, OutputStream outputStream);

    /**
     * list file info for upload/download
     * @param requestParamsVo request params
     * @return
     */
    FileListVo listFiles(FileListRequestParamsVo requestParamsVo);
}
