package org.lotus.carp.webssh.config.controller.api;

import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.vo.FileDownLoadParamsVo;
import org.lotus.carp.webssh.config.controller.vo.FileListRequestParamsVo;
import org.lotus.carp.webssh.config.controller.vo.FileListVo;
import org.lotus.carp.webssh.config.controller.vo.FileUploadDataVo;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 10:40
 **/
@RequestMapping("/webssh/file")
public interface WebSshFileApi {

    /**
     * list remote path files
     *
     * @param requestParamsVo
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    default WebSshResponse<FileListVo> listFiles(@Valid FileListRequestParamsVo requestParamsVo) {
        throw new WebSshBusinessException("Please implements me!");
    }

    /**
     * download file from remote server
     *
     * @param response
     * @param fileDownLoadParamsVo
     */
    @GetMapping("/download")
    default void downLoadFile(HttpServletResponse response, @Valid FileDownLoadParamsVo fileDownLoadParamsVo) throws IOException {
        throw new WebSshBusinessException("Please implements me!");
    }

    /**
     * upload file to ssh server
     *
     * @param fileUploadDataVo
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    default WebSshResponse<Boolean> uploadFileToServer(@Valid FileUploadDataVo fileUploadDataVo, @RequestParam("file") MultipartFile file) {
        throw new WebSshBusinessException("Please implements me!");
    }
}
