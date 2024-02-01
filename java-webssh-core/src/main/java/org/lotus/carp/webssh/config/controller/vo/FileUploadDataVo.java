package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 14:11
 **/
@Data
public class FileUploadDataVo extends WebSshRequestBase {
    @NotBlank
    private String id;
    private String dir;
    @NotBlank
    private String path;
}
