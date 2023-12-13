package com.ll.medium.global.rsData;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RsData<T> {
    private String resultCode;
    private int statusCode;
    private String msg;
    private T data;

    public boolean isSuccess() {
        return getStatusCode() >= 200 && getStatusCode() < 400;
    }

    public boolean isFail() {
        return !isSuccess();
    }

    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        int statusCode = Integer.parseInt(resultCode);

        return new RsData<>(resultCode, statusCode, msg, data);
    }

    public static <T> RsData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, null);
    }

    public static <T> RsData<T> of(String resultCode, T data) {
        return of(resultCode, null, data);
    }

    public static <T> RsData<T> of(String resultCode) {
        return of(resultCode, null, null);
    }
}
