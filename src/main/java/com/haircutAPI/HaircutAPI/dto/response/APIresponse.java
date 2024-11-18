package com.haircutAPI.HaircutAPI.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIresponse<T> {


    private int code;
    private String message;


    private T result;


    public APIresponse(int code) {
        this.code = code;
    }


    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public T getResult() {
        return result;
    }


    public void setResult(T result) {
        this.result = result;
    }

    
}
