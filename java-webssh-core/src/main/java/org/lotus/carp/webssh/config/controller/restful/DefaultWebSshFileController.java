package org.lotus.carp.webssh.config.controller.restful;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.api.FileApi;
import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.vo.FileDownLoadParamsVo;
import org.lotus.carp.webssh.config.controller.vo.FileListRequestParamsVo;
import org.lotus.carp.webssh.config.controller.vo.FileListVo;
import org.lotus.carp.webssh.config.controller.vo.FileUploadDataVo;
import org.lotus.carp.webssh.config.service.WebSshFileService;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
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
public class DefaultWebSshFileController extends BaseController implements FileApi {

    @Resource
    private WebSshLoginService webSshLoginService;

    @Resource
    private WebSshFileService webSshFileService;




    @Override
    public WebSshResponse<FileListVo> listFiles(@Valid FileListRequestParamsVo requestParamsVo) {
        if (!webSshLoginService.isTokenValid(requestParamsVo.getToken())) {
            return WebSshResponse.fail("token is invalid.");
        }
        return WebSshResponse.ok(webSshFileService.listFiles(requestParamsVo),"success");
    }

    @Override
    public void downLoadFile(HttpServletResponse response, @Valid FileDownLoadParamsVo fileDownLoadParamsVo) throws IOException {
        if (!webSshLoginService.isTokenValid(fileDownLoadParamsVo.getToken())) {
            log.error("token invalid:token:{}", fileDownLoadParamsVo.getToken());
            return;
        }
        String[] fileMetas = fileDownLoadParamsVo.getPath().split("/");
        String fileName = fileMetas[fileMetas.length - 1];
        if (ObjectUtils.isEmpty(fileName)) {
            fileName = "" + System.currentTimeMillis();
        }
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(fileName, StandardCharsets.UTF_8).build().toString());
        //write file.
        webSshFileService.downloadFile(fileDownLoadParamsVo,response.getOutputStream());
        response.getOutputStream().flush();
    }

    @Override
    public WebSshResponse<Boolean> uploadFileToServer(@Valid FileUploadDataVo fileUploadDataVo, MultipartFile file) {
        if (!webSshLoginService.isTokenValid(fileUploadDataVo.getToken())) {
            return WebSshResponse.fail(Boolean.FALSE, "token is invalid.");
        }

        return WebSshResponse.ok(Boolean.TRUE);
    }
}
