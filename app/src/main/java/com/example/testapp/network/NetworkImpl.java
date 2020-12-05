package com.example.testapp.network;

import android.util.Log;

import com.example.testapp.MyApplication;
import com.example.testapp.R;
import com.example.testapp.model.UserResponse;

import org.jetbrains.annotations.NotNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NetworkImpl {
    // Log Tag
    private static final String TAG = "Service";
    private static CompositeDisposable disposable;
    private final NetworkService networkService;

    public NetworkImpl() {
        networkService = ApiClient.getClient().create(NetworkService.class);
        if (disposable == null) {
            disposable = new CompositeDisposable();
        }
    }

    public void getTestApiResp(@NotNull NetworkCallback<UserResponse> callback) {
        callback.getDisposable(networkService.callTestApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponse>() {
                    @Override
                    public void onNext(@NotNull UserResponse commonResponse) {
                        try {
                            if(commonResponse.getCode()==200) {
                                callback.onSuccess(commonResponse);
                            }else{
                                callback.onError(MyApplication.getContext().getString(R.string.network_error));
                            }
                        } catch (Exception e) {
                            callback.onError(MyApplication.getContext().getString(R.string.network_error));
                            Log.e(TAG, "onNext: ", e);
                        }
                    }
                    @Override
                    public void onError(@NotNull Throwable e) {
                        callback.onError(new NetworkError(e).getAppErrorMessage());
                    }
                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete:");
                    }
                }));
    }
}
