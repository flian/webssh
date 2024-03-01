package org.lotus.carp.webssh.page.api.vo;

import lombok.Data;
import org.lotus.carp.webssh.config.controller.vo.WebSshRequestBase;

/**
 * <h3>webssh</h3>
 * <p>project exchange token params</p>
 *
 * @author : foy
 * @date : 2024-03-01 12:21
 **/
@Data
public class ProjectHeaderRequestVo extends WebSshRequestBase {
    private String projectExchangeToken;
}
