package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 11:28
 **/
@Data
public class FileListRequestParamsVo extends WebSshRequestBase {
    @NotBlank
    private String path;
}
