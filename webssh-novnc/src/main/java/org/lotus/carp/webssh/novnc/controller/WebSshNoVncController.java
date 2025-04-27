package org.lotus.carp.webssh.novnc.controller;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.restful.BaseController;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.novnc.common.WebSshNoVncConst;
import org.lotus.carp.webssh.novnc.controller.vo.TargetHostCurrentVncInfoResultVo;
import org.lotus.carp.webssh.novnc.websocket.NoVncWebSocketHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private static final String VNC_LITE_FILE_STR = "vnc_lite.html";

    @Resource
    private WebSshLoginService webSshLoginService;

    @Resource(name = "noVncWebSocketHandler")
    private NoVncWebSocketHandler noVncWebSocketHandler;

    @GetMapping("/index")
    public String indexPage(@RequestParam(value = "token",required = false) String token,
                            @RequestParam(value = "host") String tryHost,
                            @RequestParam(value = "port") String trtPort,
                            @RequestParam(value = "linux",required = false,defaultValue = "false") boolean isLinux,
                            @RequestParam(value = "vncLite",required = false,defaultValue = "false") boolean vncLite){
        //suggest install tightVNC in linux/windows
        //for linux install please refer：https://www.cnblogs.com/saneri/p/15136590.html
        //for windows install and config. TBD
        ensureToken(webSshLoginService,token);
        String page = VNC_INDEX_FILE_STR;
        if(vncLite){
            page = VNC_LITE_FILE_STR;
        }
        //current using noVNC version 1.6.0
        return String.format("%s/%s?token=%s&host=%s&port=%s&linux=%s"
                ,WebSshNoVncConst.WEB_SSH_NO_VNC_PREFIX,page,token,tryHost,trtPort,isLinux);
    }


    @GetMapping("/host/info")
    public WebSshResponse<TargetHostCurrentVncInfoResultVo> info(@RequestParam(value = "token",required = false) String token,
                                                                 @RequestParam(value = "host") String tryHost,
                                                                 @RequestParam(value = "linux",required = false,defaultValue = "false") boolean isLinux,
                                                                 @RequestParam(value = "genVncIndexUrl",defaultValue = "false") boolean genVncIndexUrl){
        ensureToken(webSshLoginService,token);
        TargetHostCurrentVncInfoResultVo result = new TargetHostCurrentVncInfoResultVo();
        result.setHost(tryHost);
        result.setLinux(isLinux);
        Integer currentHostPort = noVncWebSocketHandler.getCurrentHost(tryHost);
        int tryPort = isLinux?5901:5900;
        if(null != currentHostPort){
            tryPort = currentHostPort+1;
        }
        result.setNextPort(tryPort);
        if(genVncIndexUrl){
            result.setVncHtmlUrl(String.format("%s/%s?token=%s&host=%s&port=%s&linux=%s"
                    ,WebSshNoVncConst.WEB_SSH_NO_VNC_PREFIX,"/index",token,result.getHost(),result.getNextPort(),isLinux));
        }
        return WebSshResponse.ok(result);
    }
}
