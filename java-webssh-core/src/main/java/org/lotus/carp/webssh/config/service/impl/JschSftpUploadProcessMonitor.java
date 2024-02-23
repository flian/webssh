package org.lotus.carp.webssh.config.service.impl;

import com.jcraft.jsch.SftpProgressMonitor;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h3>javaWebSSH</h3>
 * <p>jsch upload process monitor</p>
 *
 * @author : foy
 * @date : 2024-02-23 15:57
 **/
public class JschSftpUploadProcessMonitor implements SftpProgressMonitor {

    private static Map<String, JschSftpUploadProcessMonitor> monitorMap = new ConcurrentHashMap<>();
    private long maxCount = 0;
    private long uploaded = 0;

    private String fileId;

    private long startTms = System.currentTimeMillis();
    //should remove before time. 2 hours.
    private long shouldRemoveTms = startTms + 2 * 60 * 60 * 1000L;

    public JschSftpUploadProcessMonitor(String fileId) {
        this(fileId, 0);
    }

    public JschSftpUploadProcessMonitor(String fileId, long max) {
        if (ObjectUtils.isEmpty(fileId)) {
            throw new WebSshBusinessException("fileId should not empty!");
        }
        if (max > 0) {
            this.maxCount = max;
        }
        this.fileId = fileId;
    }

    @Override
    public void init(int op, String src, String dest, long max) {
        if (max > 0) {
            this.maxCount = max;
        }
        //add to cache.
        monitorMap.put(fileId, this);
    }

    @Override
    public boolean count(long count) {
        if (count > 0) {
            uploaded += count;
            if (maxCount > 0 && uploaded >= maxCount) {
                end();
            }
            return true;
        }
        return false;
    }

    @Override
    public void end() {
        //remove from cache
        monitorMap.remove(fileId);
    }

    public static boolean isFileUploading(String fileId) {
        return monitorMap.containsKey(fileId);
    }

    public static long uploadedSize(String fileId) {
        return isFileUploading(fileId) ? monitorMap.get(fileId).uploaded : 0;
    }
}
