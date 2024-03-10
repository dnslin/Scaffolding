package in.dnsl.exception;

import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    /**
     *  如果抛出的是 AppException 的增强
     *  返回值 就是返回的页面
     *  参数： controller 抛出的异常
     */
    @ExceptionHandler(AppException.class)
    @ResponseBody
    public Wrapper<?> appExceptionHandler(AppException ex, HttpServletRequest request){
        return WrapMapper.ok(ex.getMessage());
    }

    /**
     *  如果抛出的是Exception 的增强
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Wrapper<?> exceptionHandler(Exception ex, HttpServletRequest request){
        log.error("发生未知异常",ex);
        return WrapMapper.ok(ex.getMessage());
    }

}