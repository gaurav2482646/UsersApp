package com.example.testapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.databinding.UserItemBinding;
import com.example.testapp.model.UserResponse;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {


    private List<UserResponse.User> userList = new ArrayList<>();

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemBinding binding = UserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);

    }

    public void setData(List<UserResponse.User> data) {
        this.userList = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, gender, email, statusOffline, statusOnline;

        public UserViewHolder(@NonNull UserItemBinding itemView) {
            super(itemView.getRoot());
            name = itemView.userName;
            gender = itemView.userGender;
            email = itemView.userEmail;
            statusOffline = itemView.statusOffline;
            statusOnline = itemView.statusOnline;
        }

        void bind(UserResponse.User user) {
            name.setText(user.getName());
            gender.setText(user.getGender());
            email.setText(user.getEmail());
            if (user.getStatus().equalsIgnoreCase("Active")) {
                statusOffline.setVisibility(View.GONE);
                statusOnline.setVisibility(View.VISIBLE);
            } else {
                statusOffline.setVisibility(View.VISIBLE);
                statusOnline.setVisibility(View.GONE);
            }
        }
    }
}
