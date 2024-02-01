package org.lotus.carp.webssh.config.controller.api;

import org.lotus.carp.webssh.config.controller.vo.FileDownLoadParamsVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 10:40
 **/
@RequestMapping("/webssh/file")
public interface FileApi {

    @GetMapping("/download")
    void downLoadFile(HttpServletResponse response, FileDownLoadParamsVo fileDownLoadParamsVo);
}
