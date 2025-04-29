package org.lotus.carp.webssh.config.service;

import java.io.InputStream;

/**
 * <h3>webssh</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-05-07 16:14
 **/
public interface WebSshJavaRdpService {
    /**
     * return proper java rpd jar file stream
     * @return
     */
   default InputStream getProperJavaRdpJarStream(){
        return null;
    }
}
