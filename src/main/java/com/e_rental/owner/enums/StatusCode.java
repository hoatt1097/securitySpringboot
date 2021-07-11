package com.e_rental.owner.enums;

public enum StatusCode {
    BAD_REQUEST("BAD_REQUEST"),
    ACCESS_DENY("ACCESS_DENY"),
    SUCCESS("SUCCESS");

    private String value;

    private StatusCode(String value) {
        this.value = value;
    }

    public String getCode(){
        return this.value;
    }
}
