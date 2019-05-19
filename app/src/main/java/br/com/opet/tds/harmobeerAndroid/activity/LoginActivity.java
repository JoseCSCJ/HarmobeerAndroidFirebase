package br.com.opet.tds.harmobeerAndroid.activity;

import android.os.Bundle;

import android.view.View;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.opet.tds.harmobeerAndroid.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText editEmail, editPassword;
    private Button signIn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.email_sign_in_button);


    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if(mUser != null){
           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
           intent.putExtra("idUsuarioLogado", mUser.getEmail());
           startActivity(intent);
        }
    }

    public void signIn(View view) {
        String email = editEmail.getText().toString();
        String senha = editPassword.getText().toString();


        mAuth.signInWithEmailAndPassword(email,senha)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("idUsuarioLogado", editEmail.getText().toString());
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Erro ao Logar.", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    }


