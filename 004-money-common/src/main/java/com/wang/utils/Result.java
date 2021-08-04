package com.wang.utils;

/**
 * 注册页面：返回值包装类
 * @author 毛能能
 */

public class Result {

//    成功为1，失败为0
    private Integer code;

    private String message;

    public static Result success(String message){

        Result result = new Result();
        result.setCode(1);
        result.setMessage(message);
        return result;
    }

    public static Result unSuccess (String message){

        Result result = new Result();
        result.setCode(0);
        result.setMessage(message);
        return result;
    }




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
