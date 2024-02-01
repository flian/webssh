package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;

import java.util.List;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 11:23
 **/
@Data
public class FileListVo {
    private String path;
    private List<FileMetaVo> list;
}
