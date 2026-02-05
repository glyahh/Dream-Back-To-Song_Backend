package com.dbts.dreambacktosong_backend.common;

/**
 * 业务异常
 *
 * <p>用于在业务逻辑中抛出可预期错误，由全局异常处理器转换为统一的 ApiResponse。
 */
public class BizException extends RuntimeException {

    /** 业务错误码 */
    private final int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

