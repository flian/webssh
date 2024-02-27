package org.lotus.carp.webssh;

import org.lotus.carp.webssh.page.common.WebSshVue2PageConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * <h3>webssh standalone start module</h3>
 * <p>webssh server start</p>
 *
 * @author : foy
 * @date : 2024-01-31 09:36
 **/
@SpringBootApplication
public class WebSshServerApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(WebSshServerApplication.class, args);
        Environment environment = configurableApplicationContext.getBean(Environment.class);
        String ip = InetAddress.getLocalHost().getHostAddress();
        String[] hosts = {"localhost", "127.0.0.1", ip};
        String contextPath = environment.getProperty("server.servlet.context-path");
        String indexUri = composeIndexUri(contextPath);
        String serverPort = environment.getProperty("server.port");
        System.out.println("\n\n =================系统启动成功！后台地址：====================== ");
        Arrays.stream(hosts).forEach(h -> {
            System.out.println(String.format("http://%s:%s%s", h, serverPort, indexUri));
        });
        System.out.println("===================================================== ");

    }

    private static String composeIndexUri(String contextPath) {
        if (null == contextPath || contextPath.isEmpty()) {
            return WebSshVue2PageConst.WEB_SSH_VUE2_INDEX;
        }
        return contextPath + WebSshVue2PageConst.WEB_SSH_VUE2_INDEX;

    }
}
