package com.example.testapp.util;

public class Resource<T> {
    private final Status status;
    private final T data;
    private final String message;

    private Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String message, T data) {
        return new Resource<>(Status.ERROR, data, message);
    }


    public static <T> Resource<T> authError(String message) {
        return new Resource<>(Status.AUTH_ERROR, null, message);
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        SUCCESS,
        ERROR,
        AUTH_ERROR
    }

}

