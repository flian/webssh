package org.lotus.carp.webssh.page.api.vo;

import lombok.Data;

/**
 * <h3>webssh</h3>
 * <p>project token header vo</p>
 *
 * @author : foy
 * @date : 2024-03-01 10:20
 **/
@Data
public class ProjectHeaderParamVo {
    private String name;
    private String value;

    public ProjectHeaderParamVo(String name, String val) {
        this.name = name;
        this.value = val;
    }
}