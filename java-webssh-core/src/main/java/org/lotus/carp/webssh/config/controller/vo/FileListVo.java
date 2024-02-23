package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;

import java.util.List;

import static org.lotus.carp.webssh.config.controller.common.WebSshAppConst.DEFAULT_WEBSSH_SUCCESS_MSG;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 11:23
 **/
@Data
//@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class FileListVo {
    private String path;
    private List<FileMetaVo> list;
    private String msg = DEFAULT_WEBSSH_SUCCESS_MSG;
}
