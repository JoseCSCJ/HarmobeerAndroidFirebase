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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.opet.tds.harmobeerAndroid.R;
import br.com.opet.tds.harmobeerAndroid.model.Prato;


public class PratoFragment extends Fragment {

    private EditText nomeprato;
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
        View view = inflater.inflate(R.layout.fragment_prato, null);
        Button mButton = (Button) view.findViewById(R.id.cadastrarPrato);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nomeprato = getActivity().findViewById(R.id.nomeprato);

                    Prato prato = new Prato();

                    String idUsuario = (String) getActivity().getIntent().getSerializableExtra("idUsuarioLogado");

                    prato.setNome(nomeprato.getText().toString());
                    prato.setUsuarioId(idUsuario);

                    if(!prato.getNome().isEmpty()) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("criador", prato.getUsuarioId());
                        data.put("nomeprato", prato.getNome());

                        db.collection("pratos")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                          @Override
                                                          public void onSuccess(DocumentReference documentReference) {
                                                              Toast.makeText(getActivity().getApplicationContext(),
                                                                      "O prato foi adicionado com sucesso!",
                                                                      Toast.LENGTH_SHORT).show();
                                                          }
                                                      }
                                ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Não foi possível adicionar uma novo prato no banco de dados",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                    else{
                        throw new Exception();
                    }
                }
                catch (Throwable t){
                    t.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "O prato não pode ser adicionado",
                            Toast.LENGTH_SHORT).show();
                }

                nomeprato.setText("");

            }
        });
        return view;
    }



}
