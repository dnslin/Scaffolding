package in.dnsl.exception;

import in.dnsl.core.WrapMapper;
import in.dnsl.core.Wrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

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
        return WrapMapper.error(ex.getMessage());
    }

    /**
     *  如果抛出的是Exception 的增强
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Wrapper<?> exceptionHandler(Exception ex, HttpServletRequest request){
        log.error("发生未知异常",ex);
        return WrapMapper.error("发生未知异常");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Wrapper<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return WrapMapper.wrap(500, "数据验证错误",errors);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoHandlerFoundException(NoResourceFoundException ex) {
        log.error("404异常",ex);
        return "redirect:/error/404";
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Wrapper<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error("请求类型错误",ex);
        return WrapMapper.error("请求类型错误");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Wrapper<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("请求参数错误",ex);
        return WrapMapper.error("请求参数错误");
    }
}