package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;

import static org.lotus.carp.webssh.config.controller.common.WebSshAppConst.DEFAULT_WEB_SSH_SUCCESS_MSG;

/**
 * <h3>javaWebSSH</h3>
 * <p>file upload result vo</p>
 *
 * @author : foy
 * @date : 2024-02-23 15:10
 **/
@Data
public class FileUploadResultVo {

    private boolean ok;
    private String msg = DEFAULT_WEB_SSH_SUCCESS_MSG;

    public static FileUploadResultVo failure(String msg) {
        FileUploadResultVo result = new FileUploadResultVo();
        result.setOk(false);
        result.setMsg(msg);
        return result;
    }
}
