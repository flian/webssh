package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 10:42
 **/
@Data
public class FileDownLoadParamsVo extends WebSshRequestBase {
    @NotBlank
    private String path;

}
