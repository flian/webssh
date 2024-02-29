package org.lotus.carp.webssh.config.service.impl;

import com.jcraft.jsch.Session;

/**
 * <h3>javaWebSSH</h3>
 * <p>ensure session is closed after session create and call</p>
 *
 * @author : foy
 * @date : 2024-02-23 09:45
 **/
public interface EnsureCloseSessionCall {
    void callSession(Session sftp);
}
