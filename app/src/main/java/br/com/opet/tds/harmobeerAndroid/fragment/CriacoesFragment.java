package br.com.opet.tds.harmobeerAndroid.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


import br.com.opet.tds.harmobeerAndroid.R;
import br.com.opet.tds.harmobeerAndroid.activity.AtualizarCerv;
import br.com.opet.tds.harmobeerAndroid.activity.AtualizarPrato;
import br.com.opet.tds.harmobeerAndroid.activity.MainActivity;
import br.com.opet.tds.harmobeerAndroid.adapter.CervejaAdapter;
import br.com.opet.tds.harmobeerAndroid.adapter.PratoAdapter;
import br.com.opet.tds.harmobeerAndroid.adapter.UsuarioAdapter;
import br.com.opet.tds.harmobeerAndroid.model.Cerveja;
import br.com.opet.tds.harmobeerAndroid.model.Prato;
import br.com.opet.tds.harmobeerAndroid.model.Usuario;


public class CriacoesFragment extends Fragment {

    private Spinner listaUsuarios;
    private ListView listaCervejas, listaPratos;
    private Cerveja cerveja;
    private Prato prato;
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
        View view = inflater.inflate(R.layout.fragment_criacoes, null);
        listaUsuarios = view.findViewById(R.id.viewUser);
        listaCervejas = view.findViewById(R.id.viewCerv);
        listaPratos = view.findViewById(R.id.viewPrato);

                try {
                    db.collection("usuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Usuario> u = new ArrayList<Usuario>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Usuario usu = new Usuario();
                                    usu.setEmail(document.getString("email"));
                                    usu.setUsername(document.getString("username"));
                                    u.add(usu);
                                    if (getActivity() != null) {
                                        final UsuarioAdapter adapter = new UsuarioAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, u);
                                        listaUsuarios.setAdapter(adapter);
                                    }
                                    listaUsuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            try {
                                                final Usuario usuario = (Usuario) adapterView.getItemAtPosition(i);
                                                db.collection("pratos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            ArrayList<Prato> p = new ArrayList<Prato>();
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                if (usuario.getEmail().equals(document.get("criador").toString())) {
                                                                    Prato prato = new Prato();
                                                                    prato.setId(document.getId());
                                                                    prato.setNome(document.getString("nomeprato"));
                                                                    p.add(prato);
                                                                }
                                                                if (getActivity() != null) {
                                                                    PratoAdapter adapter = new PratoAdapter(getActivity().getApplicationContext(), R.layout.prato_item, p);
                                                                    listaPratos.setAdapter(adapter);
                                                                }
                                                                listaPratos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                    @Override
                                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                                        Prato prato = (Prato) adapterView.getItemAtPosition(i);
                                                                        callActivityPrato(prato.getId());

                                                                    }

                                                                });
                                                            }
                                                        }else {
                                                            Toast.makeText(getActivity().getApplicationContext(),
                                                                    "Não foi possível recuperar os dados do Firebase", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                db.collection("cervejas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            ArrayList<Cerveja> c = new ArrayList<Cerveja>();
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                if(usuario.getEmail().equals(document.get("criador").toString())) {
                                                                    Cerveja cerv = new Cerveja();
                                                                    cerv.setID(document.getId());
                                                                    cerv.setEstilo(document.getString("estilo"));
                                                                    cerv.setNome(document.getString("nomecerv"));
                                                                    cerv.setTeor_alc(document.getDouble("teor"));
                                                                    c.add(cerv);
                                                                }
                                                                if (getActivity() != null) {
                                                                    CervejaAdapter adapter = new CervejaAdapter(getActivity().getApplicationContext(), R.layout.cerveja_item, c);
                                                                    listaCervejas.setAdapter(adapter);
                                                                }
                                                                listaCervejas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                    @Override
                                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                                        Cerveja cerveja= (Cerveja) adapterView.getItemAtPosition(i);
                                                                        callActivityCerv (cerveja.getID());
                                                                    }


                                                                });
                                                            }

                                                        } else {
                                                            Toast.makeText(getActivity().getApplicationContext(),
                                                                    "Não foi possível recuperar os dados do Firebase", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });;
                                            } catch (Throwable t) {
                                                t.printStackTrace();
                                                Toast.makeText(getActivity().getApplicationContext(), "Aconteceu um erro... Tente mais tarde", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }

                            } else {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Não foi possível recuperar os dados do Firebase", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Aconteceu um erro... Tente mais tarde", Toast.LENGTH_SHORT).show();
                }

        return view;


    }
    private void callActivityCerv (String documentId) {

        Intent atualizar = new Intent(getActivity().getApplicationContext(),AtualizarCerv.class);
        atualizar.putExtra("ID",documentId);
        atualizar.putExtra("idUsuarioLogado", ((MainActivity)getActivity()).retornaUsuarioLogado());
        startActivity(atualizar);
    }

    private void callActivityPrato (String id) {
        Intent atualizar = new Intent(getActivity().getApplicationContext(),AtualizarPrato.class);
        atualizar.putExtra("ID",id);
        atualizar.putExtra("idUsuarioLogado", ((MainActivity)getActivity()).retornaUsuarioLogado());
        startActivity(atualizar);
    }


}


