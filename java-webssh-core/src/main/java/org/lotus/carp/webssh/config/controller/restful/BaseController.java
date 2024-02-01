package org.lotus.carp.webssh.config.controller.restful;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 16:33
 **/
@Slf4j
public class BaseController {

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
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public WebSshResponse handleBusinessException(BusinessException ex) {
        log.error("BusinessException got. ",ex);
        return WebSshResponse.fail(ex.getMessage());
    }

}
