package org.lotus.carp.webssh.config.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RdpValidResult {
    private boolean isOk;
    private String errorMsg;

    public static RdpValidResult RdpValidResultError(String errorMsg) {
        RdpValidResult resultError = new RdpValidResult();
        resultError.isOk = false;
        resultError.errorMsg = errorMsg;
        return resultError;
    }

    public final static RdpValidResult OK = new RdpValidResult(true, null);
}
