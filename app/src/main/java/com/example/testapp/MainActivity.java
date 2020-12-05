package com.example.testapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.testapp.adapters.UserListAdapter;
import com.example.testapp.databinding.ActivityMainBinding;
import com.example.testapp.model.UserResponse;
import com.example.testapp.util.Resource;
import com.example.testapp.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    MainViewModel viewModel;
    ActivityMainBinding binding;
    UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setupList();
        callAPI();
        binding.getRoot().setOnRefreshListener(this::callAPI);
    }

    void setupList() {
        userListAdapter = new UserListAdapter();
        binding.list.setAdapter(userListAdapter);
        binding.list.setHasFixedSize(true);
    }

    private void callAPI() {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.getUserList().observe(this, this::onChanged);
    }


    @SuppressLint("SetTextI18n")
    private void onChanged(Resource<UserResponse> resource) {
        binding.progressBar.setVisibility(View.GONE);
        binding.getRoot().setRefreshing(false);
        if (resource.getStatus() == Resource.Status.SUCCESS) {
            Log.d(TAG, "callAPI: " + resource.getData().toString());
            UserResponse testResponse = resource.getData();
            userListAdapter.setData(testResponse.getData());
        } else {
            Toast.makeText(this, resource.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}