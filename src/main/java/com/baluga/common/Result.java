package com.baluga.common;

import lombok.Data;

/**
 * 统一API响应结果封装类
 * @param <T> 数据类型
 */
@Data
public class Result<T> {
    /**
     * 状态码：200成功，其他失败
     */
    private Integer code;
    
    /**
     * 响应信息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;

    private Result() {}

    /**
     * 成功返回
     * @param data 数据
     * @param <T> 数据类型
     * @return Result
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 失败返回
     * @param message 错误信息
     * @return Result
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
}
