package org.lotus.carp.webssh.config.service.impl;

import com.jcraft.jsch.ChannelSftp;

/**
 * <h3>javaWebSSH</h3>
 * <p>ensure  sftp is clossed after call</p>
 *
 * @author : foy
 * @date : 2024-02-23 09:43
 **/
public interface EnsureCloseSftpCall {
    void callSftp(ChannelSftp sftp);
}
