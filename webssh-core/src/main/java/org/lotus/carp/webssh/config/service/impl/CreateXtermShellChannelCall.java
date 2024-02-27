package org.lotus.carp.webssh.config.service.impl;

import java.io.InputStream;
import java.io.OutputStream;

public interface CreateXtermShellChannelCall {
    /**
     * get xterm shell inputStream and outputStream
     * @param channelInputStream
     * @param channelOutputStream
     */
    void applySetInputAndOutStream(InputStream channelInputStream, OutputStream channelOutputStream);
}
