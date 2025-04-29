package org.lotus.carp.webssh.config.controller.restful;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.api.WebSshFileApi;
import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.vo.*;
import org.lotus.carp.webssh.config.service.WebSshFileService;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 14:17
 **/
@Slf4j
public class DefaultWebSshFileController extends BaseController implements WebSshFileApi {

    @Resource
    private WebSshLoginService webSshLoginService;

    @Resource
    private WebSshFileService webSshFileService;


    @Override
    public WebSshResponse<FileListVo> listFiles(@Valid FileListRequestParamsVo requestParamsVo) {
        ensureToken(webSshLoginService,requestParamsVo.getToken());

        long startTms = System.currentTimeMillis();
        FileListVo result = webSshFileService.listFiles(requestParamsVo);
        long endTms = System.currentTimeMillis();
        return WebSshResponse.ok(result, result.getMsg(), String.format("%ss", (endTms - startTms) / 1000));
    }

    @Override
    public void downLoadFile(HttpServletResponse response, @Valid FileDownLoadParamsVo fileDownLoadParamsVo) throws IOException {
        ensureToken(webSshLoginService,fileDownLoadParamsVo.getToken());

        String[] fileMetas = fileDownLoadParamsVo.getPath().split("/");
        String fileName = fileMetas[fileMetas.length - 1];
        if (ObjectUtils.isEmpty(fileName)) {
            fileName = "" + System.currentTimeMillis();
        }
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(fileName, StandardCharsets.UTF_8).build().toString());
        //write file.
        webSshFileService.downloadFile(fileDownLoadParamsVo, response.getOutputStream());
        response.getOutputStream().flush();
    }

    @Override
    public WebSshResponse<Boolean> uploadFileToServer(@Valid FileUploadDataVo fileUploadDataVo, @RequestParam("file") MultipartFile file) {
        ensureToken(webSshLoginService,fileUploadDataVo.getToken());
        long startTms = System.currentTimeMillis();
        //upload files.
        FileUploadResultVo uploadResultVo = webSshFileService.uploadFile(fileUploadDataVo, file);
        long endTms = System.currentTimeMillis();
        return WebSshResponse.ok(uploadResultVo.isOk(), uploadResultVo.getMsg(), String.format("%ss", (endTms - startTms) / 1000));
    }
}
