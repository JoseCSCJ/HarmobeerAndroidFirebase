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

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import java.util.Map;


import br.com.opet.tds.harmobeerAndroid.R;
import br.com.opet.tds.harmobeerAndroid.model.Cerveja;


public class CervFragment extends Fragment {

    private EditText nome, estilo, teor;
    private FirebaseFirestore db;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cerv, null);
        Button mButton = view.findViewById(R.id.cadastrarCerv);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nome = getActivity().findViewById(R.id.nomecerv);
                    estilo = getActivity().findViewById(R.id.estilo);
                    teor = getActivity().findViewById(R.id.teor);

                    Cerveja cerveja = new Cerveja();

                    String idUsuario = (String) getActivity().getIntent().getSerializableExtra("idUsuarioLogado");

                    cerveja.setNome(nome.getText().toString());
                    cerveja.setEstilo(estilo.getText().toString());
                    cerveja.setTeor_alc(Double.parseDouble(teor.getText().toString()));
                    cerveja.setUsuarioId(idUsuario);

                    if(!cerveja.getNome().isEmpty() && !cerveja.getEstilo().isEmpty()) {

                        Map<String, Object> data = new HashMap<>();
                        data.put("criador", cerveja.getUsuarioId());
                        data.put("estilo", cerveja.getEstilo() );
                        data.put("nomecerv", cerveja.getNome());
                        data.put("teor", cerveja.getTeor_alc());
                        db.collection("cervejas")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "A cerveja foi adicionada com sucesso!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Não foi possível adicionar uma nova cerveja no banco de dados",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else{
                        throw new Exception();
                    }

                }catch (Throwable t){
                    t.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Não foi possível adicionar uma nova cerveja",
                            Toast.LENGTH_SHORT).show();
                }
                nome.setText("");
                estilo.setText("");
                teor.setText("");
            }
        });
        return view;
    }



}
