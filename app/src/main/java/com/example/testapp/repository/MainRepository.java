package com.example.testapp.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.testapp.model.UserResponse;
import com.example.testapp.network.NetworkCallback;
import com.example.testapp.network.NetworkImpl;
import com.example.testapp.util.Resource;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainRepository {    private CompositeDisposable mDisposable;

    public MainRepository() {
        mDisposable = new CompositeDisposable();
    }

    private static final String TAG = "EditRepository";

    public LiveData<Resource<UserResponse>> getUserList() {
        MutableLiveData<Resource<UserResponse>> liveData = new MutableLiveData<>();
        new NetworkImpl().getTestApiResp(new NetworkCallback<UserResponse>() {
            @Override
            public void onSuccess(@Nullable UserResponse data) {
                if (data != null)
                    liveData.postValue(Resource.success(data));
                else {
                    Log.i(TAG, "onSuccess: Returns null");
                    liveData.postValue(Resource.error("Something went wrong", null));
                }
            }

            @Override
            public void onError(String message) {
                liveData.postValue(Resource.error(message, null));
            }

            @Override
            public void getDisposable(@NotNull Disposable disposable) {
                mDisposable.add(disposable);
            }
        });
        return liveData;
    }

}
