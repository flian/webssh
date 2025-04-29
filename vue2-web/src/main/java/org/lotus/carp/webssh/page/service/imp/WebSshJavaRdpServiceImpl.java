package org.lotus.carp.webssh.page.service.imp;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.service.WebSshJavaRdpService;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.lotus.carp.webssh.page.common.WebSshVue2PageConst;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;

/**
 * <h3>webssh</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-05-07 16:17
 **/
@Slf4j
@Service
public class WebSshJavaRdpServiceImpl implements WebSshJavaRdpService {
    @Resource
    protected WebSshConfig webSshConfig;

    @Override
    public InputStream getProperJavaRdpJarStream() {
        String fileName = webSshConfig.getProperJavaRdpJar();
        String filePath = WebSshVue2PageConst.WEB_SSH_JAVA_RDP_STATIC_FILE_ROOT + File.separator + fileName;
        InputStream in = WebSshJavaRdpServiceImpl.class.getClassLoader().getResourceAsStream(filePath);
        return in;
    }
}
