package com.haircutAPI.HaircutAPI.ENUM;

public enum ErrorCode {
    DATA_INPUT_INVALID(401, "Your Input Data is invalid"),
    USERNAME_EXISTED(402, "This username is taken"),
    ID_NOT_FOUND(403, "This ID doesn't exist"),
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized error!!!"),
    USERNAME_LENGTH_INVALID(501, "Username length must be at least 6 characters"),
    PASSWORD_LENGTH_INVALID(502, "Password must be at least 8 characters"),
    EMAIL_INVALID(503, "This email is not correct!!!")
    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}