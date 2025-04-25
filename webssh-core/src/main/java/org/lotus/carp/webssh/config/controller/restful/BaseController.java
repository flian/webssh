package org.lotus.carp.webssh.config.controller.restful;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 16:33
 **/
@Slf4j
public class BaseController {

    /**
     * check and ensure current user token is valid.
     * @param webSshLoginService login server check bean instance
     * @param token current user token.
     */
    public void ensureToken(WebSshLoginService webSshLoginService,String token){
        if(!webSshLoginService.isTokenValid(token)){
            log.error("token is not valid..");
            throw  new WebSshBusinessException("invalid access. reason: invalid token.");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public WebSshResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            sb.append(String.format("%s:%s;",fieldName, errorMessage));
        });
        return WebSshResponse.fail(sb.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebSshBusinessException.class)
    @ResponseBody
    public WebSshResponse handleBusinessException(WebSshBusinessException ex) {
        log.error("BusinessException got. ",ex);
        return WebSshResponse.fail(ex.getMessage());
    }

}
