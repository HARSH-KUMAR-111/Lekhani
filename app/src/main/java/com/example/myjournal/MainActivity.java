package com.example.myjournal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button login_btn;
    Button create_acc_btn;
    private EditText emailLET;
    private EditText passET;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_btn=findViewById(R.id.button);
        create_acc_btn=findViewById(R.id.button1);

        emailLET=findViewById(R.id.email);
        passET=findViewById(R.id.password);
        create_acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}