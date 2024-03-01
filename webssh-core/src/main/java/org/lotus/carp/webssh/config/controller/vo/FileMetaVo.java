package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;
import org.lotus.carp.webssh.config.utils.WebSshUtils;

import java.util.Date;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 11:24
 **/
@Data
//@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class FileMetaVo {
    //File{Name: mFile.Name(), IsDir: mFile.IsDir(), Size: fileSize, ModifyTime: mFile.ModTime().Format("2006-01-02 15:04:05")}
    /**
     * file name
     */
    private String name;

    /**
     * is file folder
     */
    private boolean dir;

    /**
     * file human-readable size
     */
    private String size;

    /**
     * file last modify time
     */
    // @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTimeObj;

    private String modifyTime;
    /**
     * file add time
     * TODO current can't find create time from jsch. later try to find it.
     */
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTimeObj;

    private String createTime;

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

    public void setModifyTimeObj(Date modifyTimeObj) {
        this.modifyTimeObj = modifyTimeObj;
        this.modifyTime = WebSshUtils.formatDateTimeStr(modifyTimeObj);
    }

    public void setCreateTimeObj(Date createTimeObj) {
        this.createTimeObj = createTimeObj;
        this.createTime = WebSshUtils.formatDateTimeStr(createTimeObj);
    }

}
