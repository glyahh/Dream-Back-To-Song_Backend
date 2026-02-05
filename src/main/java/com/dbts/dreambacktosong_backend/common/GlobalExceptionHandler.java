package com.dbts.dreambacktosong_backend.common;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * <p>将后端异常统一转换为前端约定的返回格式，避免堆栈信息直接泄露给前端。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 处理业务异常 */
    @ExceptionHandler(BizException.class)
    public ApiResponse<Void> handleBizException(BizException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    /** 处理参数校验异常（@Valid） */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public ApiResponse<Void> handleValidationException(Exception e) {
        String msg;
        if (e instanceof MethodArgumentNotValidException manve && manve.getBindingResult().getFieldError() != null) {
            msg = manve.getBindingResult().getFieldError().getDefaultMessage();
        } else if (e instanceof BindException be && be.getBindingResult().getFieldError() != null) {
            msg = be.getBindingResult().getFieldError().getDefaultMessage();
        } else if (e instanceof ConstraintViolationException cve) {
            msg = cve.getConstraintViolations().stream().findFirst()
                    .map(cv -> cv.getMessage())
                    .orElse("参数校验失败");
        } else {
            msg = "参数校验失败";
        }
        log.warn("参数校验异常: {}", msg);
        return ApiResponse.error(400, msg);
    }

    /** 请求体 JSON 解析异常 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体解析失败", e);
        return ApiResponse.error(400, "请求体格式错误或缺失");
    }

    /** 兜底处理其他未捕获异常 */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return ApiResponse.error(500, "服务器内部错误");
    }
}

