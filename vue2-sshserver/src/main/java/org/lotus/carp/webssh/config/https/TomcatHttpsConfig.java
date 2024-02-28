package org.lotus.carp.webssh.config.https;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * <h3>webssh http force redirect to https for tomcat</h3>
 * <p>tomcat https config</p>
 *
 * @author : foy
 * @date : 2024-02-28 16:12
 **/
@Configuration
@ConditionalOnExpression("'true'.equals('${server.ssl.enabled}')" +
        "&&'true'.equals('${webssh.foreHttps}')" +
        "&&'tomcat'.equals('${webssh.underContainer}')")
public class TomcatHttpsConfig {
    @Resource
    private WebSshConfig webSshConfig;

    @Value("${server.port}")
    private int serverPort;

    @Configuration
    public class TomcatConfig {
        @Bean
        TomcatServletWebServerFactory tomcatEmbeddedServletContainerFactory() {
            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
                @Override
                protected void postProcessContext(Context context) {
                    SecurityConstraint constraint = new SecurityConstraint();
                    constraint.setUserConstraint("CONFIDENTIAL");
                    SecurityCollection collection = new SecurityCollection();
                    collection.addPattern(webSshConfig.getWebSshWebsocketPrefix() + "/*");
                    constraint.addCollection(collection);
                    context.addConstraint(constraint);
                }
            };
            tomcat.addAdditionalTomcatConnectors(createTomcatConnector());
            return tomcat;
        }

        private Connector createTomcatConnector() {
            Connector connector = new
                    Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setScheme("http");
            connector.setPort(webSshConfig.getHttpPort());
            connector.setSecure(false);
            //only ssl enabled，redirect are reasonable
            connector.setRedirectPort(serverPort);

            return connector;
        }
    }
}
