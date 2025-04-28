package org.lotus.carp.webssh;

import org.lotus.carp.webssh.page.common.WebSshVue2PageConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
        List<String> allIps = listAllIps();
        String contextPath = environment.getProperty("server.servlet.context-path");
        String sslEnabled = environment.getProperty("server.ssl.enabled");
        String protocol = "true".equalsIgnoreCase(sslEnabled) ? "https" : "http";
        String indexUri = composeIndexUri(contextPath);
        String serverPort = environment.getProperty("server.port");
        System.out.println("\n\n =================系统启动成功！后台地址：====================== ");
        allIps.stream().forEach(ip -> {
            System.out.println(String.format("%s://%s:%s%s", protocol, ip, serverPort, indexUri));
        });
        System.out.println("===================================================== ");

    }

    private static List<String> listAllIps() {
        List<String> result = new ArrayList<>();
        result.add("localhost");
        result.add("127.0.0.1");
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1) {
                        result.add(ip.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("error list networkInterfaces." + e.getMessage());
        }
        return result;
    }

    private static String composeIndexUri(String contextPath) {
        if (null == contextPath || contextPath.isEmpty()) {
            return WebSshVue2PageConst.WEB_SSH_VUE2_INDEX;
        }
        return contextPath + WebSshVue2PageConst.WEB_SSH_VUE2_INDEX;

    }
}
