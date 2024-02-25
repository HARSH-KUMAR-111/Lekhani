package com.example.myjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    EditText password_create;
    Button Sign_up_btn;
    EditText email_create;
    EditText username_create;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference collectionreference =db.collection("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        Sign_up_btn=findViewById(R.id.button2);
        username_create=findViewById(R.id.userName_create_ET);
        password_create=findViewById(R.id.password_signup);
        email_create=findViewById(R.id.email_signup);

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser =firebaseAuth.getCurrentUser();

                if(currentUser != null){
                    //hhh
                }
                else{
                    //hhh
                }
            }
        };

        Sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(email_create.getText().toString()) && !TextUtils.isEmpty(password_create.getText().toString())){
                    String email = email_create.getText().toString().trim();
                    String password = password_create.getText().toString().trim();
                    String username =username_create.getText().toString().trim();

                    CreateUserEmailAccount(email, password,username);
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CreateUserEmailAccount(String email, String password, final String username) {
        if(!TextUtils.isEmpty(email_create.getText().toString()) && !TextUtils.isEmpty(password_create.getText().toString())){

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        currentUser = firebaseAuth.getCurrentUser();
                        assert currentUser !=null;
                        final String currentUserId = currentUser.getUid();

                        Map<String, String> userObj = new HashMap<>();
                        userObj.put("userId",currentUserId);
                        userObj.put("username",username);

                        collectionreference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(Objects.requireNonNull(task.getResult().exists())){
                                            String name =task.getResult().getString("username");

                                            //if the user is registered successfully ,then move the AddJournal Activity

                                            Intent intent=new Intent(SignUpActivity.this,AddJournalActivity.class);
                                            intent.putExtra("username",name);
                                            intent.putExtra("userId",currentUserId);
                                            startActivity(intent);
                                        }
                                        else{

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Failed msg
                                        Toast.makeText(SignUpActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}