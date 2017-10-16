package app.common.handlers;

import app.common.exceptions.TipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
    created by xdCao on 2017/10/16
*/
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = TipException.class)
    public String tipException(Exception e){
        LOGGER.error("发现异常： e={}",e.getMessage());
        e.printStackTrace();
        return "comm/error_500";
    }

    @ExceptionHandler(value = Exception.class)
    public String exception(Exception e){
        LOGGER.error("发现异常： e={}",e.getMessage());
        e.printStackTrace();
        return "comm/error_404";
    }


}
