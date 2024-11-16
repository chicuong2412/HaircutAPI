package com.haircutAPI.HaircutAPI.ENUM;

public enum UserType {
    ADMIN(1, "ADMIN"), CUSTOMER(2, "CUSTOMER"), WORKER(3, "CUSTOMER");

    private int code;
    private String name;

    UserType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
