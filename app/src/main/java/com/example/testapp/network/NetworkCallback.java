package com.example.testapp.network;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.Disposable;

public interface NetworkCallback<T> {

    void onSuccess(@Nullable T data);

    void onError(String message);

    void getDisposable(@NotNull Disposable disposable);
}


