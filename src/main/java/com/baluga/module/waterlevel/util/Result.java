package com.baluga.module.waterlevel.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 鍝嶅簲鐮?*/
    private int code;

    /** 鍝嶅簲淇℃伅 */
    private String msg;

    /** 鍝嶅簲鏁版嵁 */
    private T data;

    public Result() {
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 鎴愬姛杩斿洖缁撴灉锛堝甫鏁版嵁锛?
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    /**
     * 鎴愬姛杩斿洖缁撴灉锛堟棤鏁版嵁锛?
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }

    /**
     * 澶辫触杩斿洖缁撴灉
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg(), null);
    }

    /**
     * 澶辫触杩斿洖缁撴灉锛堣嚜瀹氫箟閿欒淇℃伅锛?
     */
    public static <T> Result<T> error(String msg) {
        return new Result<>(ResultCode.ERROR.getCode(), msg, null);
    }
}

