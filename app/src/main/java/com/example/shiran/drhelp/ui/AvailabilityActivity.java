package com.example.shiran.drhelp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.shiran.drhelp.R;
import com.example.shiran.drhelp.services.FirebaseUserService;
import com.example.shiran.drhelp.services.UserService;

public class AvailabilityActivity extends AppCompatActivity {

    private SwitchCompat switchCompat_status;
    private TextView textView_status;
    private TextView textView_contactList;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        initStatusReferences();
        userService = FirebaseUserService.getInstance();

        switchCompat_status.setOnCheckedChangeListener(this::onSwitchButtonPressed);
        textView_contactList.setOnClickListener(this::onContactListButtonPressed);

    }

    private void onContactListButtonPressed(View view) {
        Intent to_contactList_Intent = new Intent(getApplicationContext(), ContactsActivity.class);
        startActivity(to_contactList_Intent);
    }

    private void onSwitchButtonPressed(CompoundButton compoundButton, boolean isChecked) {
        userService.setUserStatuse(isChecked);
        if(isChecked) {
            textView_status.setText("You are available to translate now");
        } else {
            textView_status.setText("You are unavailable to translate now");
        }
        Log.d("available", "" + isChecked);
    }

    private void initStatusReferences() {
        switchCompat_status = findViewById(R.id.status_switch);
        textView_status = findViewById(R.id.user_status_textView);
        textView_contactList = findViewById(R.id.contact_list_textView);
    }
}
