package br.com.opet.tds.harmobeerAndroid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.AuthCredential;

import com.google.firebase.auth.FirebaseAuth;

import br.com.opet.tds.harmobeerAndroid.R;
import br.com.opet.tds.harmobeerAndroid.activity.LoginActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;

import java.util.Map;

public class UsuarioFragment extends Fragment {

    private EditText username, email, senha, senhaUsu;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private AuthCredential credential;
    private boolean usuarioCriado;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        usuarioCriado = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuario, null);
        Button mButton = view.findViewById(R.id.cadastrarUsuario);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    username = getActivity().findViewById(R.id.username);
                    email = getActivity().findViewById(R.id.email);
                    senha = getActivity().findViewById(R.id.senha);

                    final String sUsername = username.getText().toString();
                    final String sEmail = email.getText().toString();
                    final String sSenha = senha.getText().toString();


                    if(!sUsername.isEmpty() && !sEmail.isEmpty() && sSenha.length()>=6 && sEmail.contains("@") && sEmail.contains(".")) {
                        criarUsuario(sEmail, sUsername);
                        mAuth.createUserWithEmailAndPassword(sEmail, sSenha)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "O novo usuario administrador "+ sUsername +" foi adicionado com sucesso! " +
                                                        "Passe os dados para que o novo administrador possa fazer seu primeiro login",
                                                Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }
                                })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "Houve uma falha no processamento desse novo usuário. Reveja os dados inseridos ou aguarde antes de tentar novamente.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Houve uma falha no processamento desse novo usuário. Reveja os dados inseridos ou aguarde antes de tentar novamente.",
                                Toast.LENGTH_SHORT).show();
                    }







                } catch (Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Não foi possível adicionar esse administrador. Reveja os dados inseridos.",
                            Toast.LENGTH_SHORT).show();
                }


            }

        });


        return view;
    }

    private void criarUsuario(String sEmail, String sUsername){
        Map<String, Object> data = new HashMap<>();
        data.put("email", sEmail);
        data.put("username", sUsername);

        db.collection("usuario")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("Adicionado ao banco de dados!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Houve uma falha na adição desse usuário no banco de dados. Será necessário adicioná-lo manualmente",
                                Toast.LENGTH_SHORT).show();
                        System.out.println("Erro ao adicionar novo usuário no banco de dados");
                    }
                });


    }
}
