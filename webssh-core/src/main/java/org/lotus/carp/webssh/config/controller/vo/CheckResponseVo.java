package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 11:37
 **/
@Data
public class CheckResponseVo {
    private boolean savePass;
    private boolean shouldVerifyToken;
}
