package com.haircutAPI.HaircutAPI.exception.DefinedException;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;

public class AppException extends RuntimeException {

    public AppException(ErrorCode errCode) {
        super(errCode.getMessage());
        this.errCode = errCode;
    }

    private ErrorCode errCode;

    public ErrorCode getErrCode() {
        return errCode;
    }

    public void setErrCode(ErrorCode errCode) {
        this.errCode = errCode;
    }

    
}
