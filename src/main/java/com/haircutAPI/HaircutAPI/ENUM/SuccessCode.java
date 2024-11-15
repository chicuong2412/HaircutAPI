package com.haircutAPI.HaircutAPI.ENUM;


public enum SuccessCode {
    CREATE_SUCCESSFUL(101, "Successful"),
    GET_DATA_SUCCESSFUL(102, "Successful"),
    UPDATE_DATA_SUCCESSFUL(103,"Successful"),
    DELETE_SUCCESSFUL(104, "Successful")
    ;

    
    SuccessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

}
