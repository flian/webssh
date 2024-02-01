package org.lotus.carp.webssh.config.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 11:24
 **/
@Data
public class FileMetaVo {
    //File{Name: mFile.Name(), IsDir: mFile.IsDir(), Size: fileSize, ModifyTime: mFile.ModTime().Format("2006-01-02 15:04:05")}
    private String name;
    private boolean isDir;
    private String size;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date ModifyTime;
}