package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 11:30
 **/

@Data
public class WebSshRequestBase {
    private String token;
    private String sshInfo;
}
