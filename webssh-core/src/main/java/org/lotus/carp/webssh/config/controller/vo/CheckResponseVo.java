package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;

/**
 * <h3>javaWebSSH</h3>
 * <p>default config for client.</p>
 *
 * @author : foy
 * @date : 2024-02-01 11:37
 **/
@Data
public class CheckResponseVo {
    /**
     * client should save password?
     */
    private boolean savePass;

    /**
     * client should verify token,if not token present,will show login form.
     */
    private boolean shouldVerifyToken;

}
