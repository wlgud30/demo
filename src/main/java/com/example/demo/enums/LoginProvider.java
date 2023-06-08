package com.example.demo.enums;

public enum LoginProvider {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao"),
    APPLE("apple");

    private String value;

    LoginProvider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
