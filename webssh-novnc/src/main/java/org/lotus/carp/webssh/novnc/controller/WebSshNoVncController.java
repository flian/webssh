package org.lotus.carp.webssh.novnc.controller;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.restful.BaseController;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.lotus.carp.webssh.novnc.common.WebSshNoVncConst;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @author : foy
 * @date : 2025/4/25:10:04
 **/
@Slf4j
@Controller
@RequestMapping(WebSshNoVncConst.WEB_SSH_NO_VNC_PREFIX)
public class WebSshNoVncController extends BaseController {

    /**
     * index with prefix parameter
     */
    private static final String VNC_INDEX_FILE_STR = "vnc.html";
    private static final String VNC_LITE_FILE_STR = "nvc_lite.html";

    @Resource
    private WebSshLoginService webSshLoginService;

    @GetMapping("/index")
    public String indexPage(@RequestParam(value = "token",required = false) String token,
                            @RequestParam(value = "host",required = false) String tryHost,
                            @RequestParam(value = "port",required = false) String trtPort,
                            @RequestParam(value = "vncLite",required = false,defaultValue = "false") boolean vncLite){
        //suggest install tightVNC in linux/windows
        //for linux install please refer：https://www.cnblogs.com/saneri/p/15136590.html
        //for windows install and config. TBD
        ensureToken(webSshLoginService,token);
        String host = "192.168.3.101";
        if(!ObjectUtils.isEmpty(tryHost)){
            host = tryHost;
        }

        String port ="5901";
        if(!ObjectUtils.isEmpty(port)){
            port = trtPort;
        }

        return String.format("%s/%s?host=%s&port=%s&password=Admin123",WebSshNoVncConst.WEB_SSH_NO_VNC_PREFIX,VNC_INDEX_FILE_STR
        ,host,port);
    }
}
