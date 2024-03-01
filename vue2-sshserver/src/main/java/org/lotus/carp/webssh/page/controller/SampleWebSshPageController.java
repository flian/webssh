package org.lotus.carp.webssh.page.controller;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.page.WebSshPageController;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <h3>webssh</h3>
 * <p>sample to override vue2-web controller</p>
 *
 * @author : foy
 * @date : 2024-03-01 14:19
 **/
@Slf4j
@Controller
public class SampleWebSshPageController extends WebSshPageController {

    /**
     * just a sample for project exchange token
     * @param request
     * @return
     */
    @Override
    public String generateProjectExchangeToken(HttpServletRequest request) {
        return "ThisIsASampleExchangeToken";
    }
}
