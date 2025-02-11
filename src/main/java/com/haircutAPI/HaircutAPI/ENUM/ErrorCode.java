package com.haircutAPI.HaircutAPI.ENUM;

public enum ErrorCode {
    DATA_INPUT_INVALID(401, "Your Input Data is invalid"),
    USERNAME_EXISTED(402, "This username is taken"),
    ID_NOT_FOUND(403, "This ID doesn't exist"),
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized error!!!"),
    USERNAME_LENGTH_INVALID(501, "Username length must be at least 6 characters"),
    PASSWORD_LENGTH_INVALID(502, "Password must be at least 8 characters"),
    EMAIL_INVALID(503, "This email is not correct!!!"),
    USERNAME_NOT_EXISTED(405, "This username doesn't exist"),
    WORKER_LOCATION_NOT_MATCHED(410, "You need to choose location matched the worker"),
    LOGIN_FAILED(404, "Password is incorrect"),
    WRONG_TOKEN(406, "This token is expired or does not exist"),
    NOTNULL(407, "The field inputs must not be null"),
    ID_CUSTOMER_NOT_FOUND(408, "Id customer doesn't exist"),
    ID_WORKER_NOT_FOUND(409, "Id worker doesn't exist"),
    ID_LOCATION_NOT_FOUND(409, "Id location doesn't exist"),
    ACCESS_DENIED(430, "You don't have permissions to access this!!!"),
    DATA_INTEGRIY(431, "Your data can't be deleted because it exists in other table of information")
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
