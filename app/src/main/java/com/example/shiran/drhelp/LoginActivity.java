package com.example.shiran.drhelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView toRegister_TextView;
    Intent toRegister_Intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toRegister_TextView = findViewById(R.id.link_to_register);
        toRegister_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               toRegister_Intent = new Intent(getApplicationContext(), RegisterActivity.class);
               startActivity(toRegister_Intent);
            }
        });

    }
}
