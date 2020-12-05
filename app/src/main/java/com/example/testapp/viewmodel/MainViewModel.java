package com.example.testapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.testapp.model.UserResponse;
import com.example.testapp.repository.MainRepository;
import com.example.testapp.util.Resource;

public class MainViewModel extends ViewModel {
    private final MainRepository repository;

    public MainViewModel() {
        repository = new MainRepository();
    }

    public LiveData<Resource<UserResponse>> getUserList() {
        return repository.getUserList();
    }
}
