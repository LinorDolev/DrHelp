package com.example.shiran.drhelp.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiran.drhelp.R;
import com.example.shiran.drhelp.entities.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i) {
        User user = userList.get(i);
        userViewHolder.textView_userName
                .setText(user.getFirstName() + " " + user.getLastName());
        userViewHolder.imageView_call.setOnClickListener(this::onCallButtonPressed);
    }

    private void onCallButtonPressed(View view) {
        Intent to_videoCaht_Intent = new Intent(this.context, VideoChatActivity.class);
        this.context.startActivity(to_videoCaht_Intent);
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textView_userName;
        ImageView imageView_user;
        ImageView imageView_call;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_userName = itemView.findViewById(R.id.user_info_textView);
            imageView_user = itemView.findViewById(R.id.user_imageView);
            imageView_call = itemView.findViewById(R.id.call_imageView);
        }
    }
}
