package org.lotus.carp.webssh.config.controller.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class FileMetaVo {
    //File{Name: mFile.Name(), IsDir: mFile.IsDir(), Size: fileSize, ModifyTime: mFile.ModTime().Format("2006-01-02 15:04:05")}
    /**
     * file name
     */
    private String name;

    /**
     * is file folder
     */
    private boolean isDir;

    /**
     * file human readable size
     */
    private String size;

    /**
     * file last modify time
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * file add time
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    /**
     * file permissions string
     */
    private String permissionsString;

    /**
     * file owner name
     */
    private String ownerName;

    /**
     * file group name
     */
    private String groupName;

    public boolean getDir(){
        return this.isDir;
    }

    @JsonProperty("IsDir")
    public boolean getIsDir(){
        return this.isDir;
    }
}
