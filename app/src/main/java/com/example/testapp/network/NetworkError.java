package com.example.testapp.network;

import java.io.IOException;
import java.util.Objects;

import retrofit2.HttpException;

public class NetworkError extends Throwable {
    private static final String DEFAULT_ERROR_MESSAGE = "Something went wrong!";
    private static final String NETWORK_ERROR_MESSAGE = "Network Issue! Try after sometime";

    private final Throwable error;
    private String serverMessage;

    public NetworkError(Throwable e) {
        super(e);
        this.error = e;
    }

    public String getMessage() {
        return error.getMessage();
    }

    public String getAppErrorMessage() {
        if (this.error instanceof IOException) return NETWORK_ERROR_MESSAGE;
        if (!(this.error instanceof HttpException)) return DEFAULT_ERROR_MESSAGE;

        retrofit2.Response<?> response = ((HttpException) this.error).response();
        if (response != null) {
            if (response.code() == 500) {
                return "Server issue!";
            } else if (response.code() == 401) {
                return "Unauthorized to call";
            } else if (response.code() == 502) {
                return "Gateway error";
            } else if (response.code() == 404) {
                return "Unable to call, try after sometime.";
            }
        }

        return DEFAULT_ERROR_MESSAGE;
    }

    public Throwable getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworkError that = (NetworkError) o;

        return Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return error != null ? error.hashCode() : 0;
    }
}