package br.com.opet.tds.harmobeerAndroid.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.opet.tds.harmobeerAndroid.R;
import br.com.opet.tds.harmobeerAndroid.model.Cerveja;


public class AtualizarCerv extends AppCompatActivity {

    private EditText nome, estilo, teor;
    private TextView dados;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_cerv);
        db = FirebaseFirestore.getInstance();

        try {
            db.collection("cervejas")
                    .document(getIntent().getStringExtra("ID"))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot document) {
                            System.out.println(document.get("nomecerv").toString()+" "+document.get("estilo").toString() + document.get("teor").toString());

                            Cerveja c = new Cerveja();
                            c.setNome(document.get("nomecerv").toString());
                            c.setEstilo(document.get("estilo").toString());
                            c.setTeor_alc(Double.parseDouble(document.get("teor").toString()));
                            dados = findViewById(R.id.dadosCerv);
                            dados.setText("Cerveja: " + c.getNome() + " Estilo: " + c.getEstilo() + " Teor " + c.getTeor_alc() + " %");
                            nome = findViewById(R.id.nomecerv);
                            estilo = findViewById(R.id.estilo);
                            teor = findViewById(R.id.teor);
                            nome.setText(c.getNome(), TextView.BufferType.EDITABLE);
                            estilo.setText(c.getEstilo(), TextView.BufferType.EDITABLE);
                            teor.setText(String.valueOf(c.getTeor_alc()), TextView.BufferType.EDITABLE);
                        }
                    });


    }catch (Throwable t){
            t.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Não foi possível carregar os dados  dessa cerveja",
                    Toast.LENGTH_SHORT).show();

        }
        Button mButton = findViewById(R.id.atualizarCerv);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Cerveja c = new Cerveja();

                    c.setNome(nome.getText().toString());
                    c.setEstilo(estilo.getText().toString());
                    c.setTeor_alc(Double.parseDouble(teor.getText().toString()));

                    if(!c.getNome().isEmpty() && !c.getEstilo().isEmpty()) {
                        db.collection("cervejas")
                                .document(getIntent().getStringExtra("ID"))
                                .update("nomecerv",c.getNome());
                        db.collection("cervejas")
                                .document(getIntent().getStringExtra("ID"))
                                .update("estilo",c.getEstilo());
                        db.collection("cervejas")
                                .document(getIntent().getStringExtra("ID"))
                                .update("teor",c.getTeor_alc());

                        Toast.makeText(getApplicationContext(),
                                "A cerveja " + c.getNome() + " foi atualizada com sucesso!",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AtualizarCerv.this, MainActivity.class);
                        intent.putExtra("idUsuarioLogado", getIntent().getStringExtra("idUsuarioLogado"));
                        startActivity(intent);
                    }
                    else{
                        throw new Exception();
                    }

                }catch (Throwable t){
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Não foi possível atualizar essa cerveja",
                            Toast.LENGTH_SHORT).show();
                }
    }

    });

        Button sButton = findViewById(R.id.removerCerv);
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db.collection("cervejas")
                            .document(getIntent().getStringExtra("ID"))
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),
                                            "A cerveja foi removida com sucesso!",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AtualizarCerv.this, MainActivity.class);
                                    intent.putExtra("idUsuarioLogado", getIntent().getStringExtra("idUsuarioLogado"));
                                    startActivity(intent);
                                }
                            });


                }catch (Throwable t){
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Não foi possível remover essa cerveja",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}

