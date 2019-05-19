package br.com.opet.tds.harmobeerAndroid.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.opet.tds.harmobeerAndroid.R;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.Map;

public class PerfilFragment extends Fragment {

    private EditText usernamePer, senhaAnt, senhaPer, senhaConf;
    private TextView emailPer;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, null);
        usernamePer = view.findViewById(R.id.usernamePer);
        emailPer = view.findViewById(R.id.emailPer);
        emailPer.setText(mUser.getEmail());
        final String idUsuario = (String)getActivity().getIntent().getSerializableExtra("idUsuarioLogado");

        db.collection("usuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> objeto = document.getData();
                        if (objeto.get("email").toString().compareTo(idUsuario) == 0) {
                            usernamePer.setText(objeto.get("username").toString());
                        }

                        }
                    }
                }
            });

        Button mButton =  view.findViewById(R.id.editarUsuario);
        Button sButton =  view.findViewById(R.id.alterarSenha);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{



                emailPer.setText(mUser.getEmail());


                if(usernamePer.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Necessário preencher para editar as informações...",
                            Toast.LENGTH_SHORT).show();
                }else {
                    db.collection("usuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String,Object> objeto = document.getData();
                                    if(objeto.get("email").toString().compareTo(idUsuario)==0){
                                        db.collection("usuario").document(document.getId())
                                                .update("username",usernamePer.getText().toString())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getActivity().getApplicationContext(),
                                                                "O seu username foi alterado para " + usernamePer.getText().toString(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity().getApplicationContext(),
                                                                "Não foi possível alterar seu username",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    };

                                }
                            }else{
                                Toast.makeText(getActivity().getApplicationContext(), "Não foi possivel recuperar os dados.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                    }


                }catch(Throwable t){
                    t.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Não foi possível efetuar as alterações",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    senhaAnt = getActivity().findViewById(R.id.senhaAnt);
                    senhaPer = getActivity().findViewById(R.id.senhaPer);
                    senhaConf = getActivity().findViewById(R.id.senhaConf);

                    AuthCredential credential = EmailAuthProvider.getCredential(mUser.getEmail(), senhaAnt.getText().toString());

                    mUser.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (senhaPer.getText().toString().equals(senhaConf.getText().toString()) &&
                                    senhaPer.getText().toString().length()>4) {
                                mUser.updatePassword(senhaPer.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "Sua senha foi alterada com sucesso.",
                                                Toast.LENGTH_SHORT).show();
                                        senhaAnt.setText("");
                                        senhaPer.setText("");
                                        senhaConf.setText("");

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "Não foi possível alterar sua senha, aguarde antes de tentar novamente",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                System.out.println(senhaPer.getText().toString().equals(senhaConf.getText().toString()));
                                System.out.println(senhaPer.getText().toString().length());
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Não foi possível alterar a sua senha. Reveja os dados inseridos.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Não foi possível alterar a sua senha. A senha atual não foi inserida corretamente",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });


                }catch (Throwable t){
                    t.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Não foi possível alterar a sua senha.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }



}
