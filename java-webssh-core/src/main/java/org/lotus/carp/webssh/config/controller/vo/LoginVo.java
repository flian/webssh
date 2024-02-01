package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-01-31 17:14
 **/
@Data
public class LoginVo {
    @NotBlank
    private String username;
    private String password;
}
