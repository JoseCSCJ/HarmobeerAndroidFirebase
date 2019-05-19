package br.com.opet.tds.harmobeerAndroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.opet.tds.harmobeerAndroid.R;
import br.com.opet.tds.harmobeerAndroid.fragment.CervFragment;
import br.com.opet.tds.harmobeerAndroid.fragment.PerfilFragment;
import br.com.opet.tds.harmobeerAndroid.fragment.PratoFragment;
import br.com.opet.tds.harmobeerAndroid.fragment.UsuarioFragment;
import br.com.opet.tds.harmobeerAndroid.fragment.CriacoesFragment;
import br.com.opet.tds.harmobeerAndroid.model.Cerveja;
import br.com.opet.tds.harmobeerAndroid.model.Prato;
import br.com.opet.tds.harmobeerAndroid.model.Usuario;


public class MainActivity extends AppCompatActivity {
     private TextView mTextMessage, emailPer;
     private Cerveja cerveja;
     private Prato prato;
     private Usuario usuario;
     private CervFragment cervFragment;
     private PratoFragment pratoFragment;
     private UsuarioFragment usuarioFragment;
     private PerfilFragment perfilFragment;
     private EditText nome, estilo, teor, nomeprato, username, email, senha, senhaUsu, usernamePer, senhaAnt, senhaPer, senhaConf;
     private Spinner listaUsuarios;
    private ListView listaCervejas, listaPratos;
    private Fragment fragment;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_cerv:
                    mTextMessage.setText(R.string.title_cerv);
                    fragment = new CervFragment();
                    return loadFragment(fragment);
                case R.id.navigation_prato:
                    mTextMessage.setText(R.string.title_prato);
                    fragment = new PratoFragment();
                    return loadFragment(fragment);
                case R.id.navigation_admin:
                    mTextMessage.setText(R.string.title_admin);
                    fragment = new UsuarioFragment();
                    return loadFragment(fragment);
                case R.id.navigation_criac:
                    mTextMessage.setText(R.string.title_criac);
                    fragment = new CriacoesFragment();
                    return loadFragment(fragment);
                case R.id.navigation_perf:
                    mTextMessage.setText(R.string.title_perf);
                    fragment = new PerfilFragment();
                    return loadFragment(fragment);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cerveja = new Cerveja();
        prato = new Prato();
        usuario = new Usuario();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final String idUsuarioLogado = (String) getIntent().getSerializableExtra("idUsuarioLogado");
        db.collection("usuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String,Object> objeto = document.getData();
                        Usuario usuarioLogado = new Usuario();
                        usuarioLogado.setEmail(objeto.get("email").toString());
                        usuarioLogado.setUsername(objeto.get("username").toString());
                        if(usuarioLogado.getEmail().compareTo(idUsuarioLogado)==0) {
                            System.out.println("O usuario " + usuarioLogado.getUsername() + " logou...");
                        }
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Não foi possivel recuperar os dados.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        nome = findViewById(R.id.nomecerv);
        estilo = findViewById(R.id.estilo);
        teor = findViewById(R.id.teor);

        nomeprato = findViewById(R.id.nomeprato);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);

        listaUsuarios = findViewById(R.id.viewUser);
        listaCervejas = findViewById(R.id.viewCerv);
        listaPratos = findViewById(R.id.viewPrato);

        usernamePer = findViewById(R.id.usernamePer);
        emailPer = findViewById(R.id.emailPer);

        senhaAnt = findViewById(R.id.senhaAnt);
        senhaPer = findViewById(R.id.senhaPer);
        senhaConf= findViewById(R.id.senhaConf);

        cervFragment = new CervFragment();
        pratoFragment = new PratoFragment();
        usuarioFragment = new UsuarioFragment();
        perfilFragment = new PerfilFragment();

        mTextMessage = findViewById(R.id.message);
        mTextMessage.setText(R.string.title_cerv);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new CervFragment());

        Button mButton = findViewById(R.id.deslogar);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                } catch (Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Não foi possível sair",
                            Toast.LENGTH_SHORT).show();

                }


            }


        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    public String retornaUsuarioLogado(){
        return (String) getIntent().getSerializableExtra("idUsuarioLogado");
    }


}


