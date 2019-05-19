package br.com.opet.tds.harmobeerAndroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.opet.tds.harmobeerAndroid.R;
import br.com.opet.tds.harmobeerAndroid.model.Prato;


public class AtualizarPrato extends AppCompatActivity {

    private EditText nome, estilo, teor;
    private TextView dados;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_prato);
        db = FirebaseFirestore.getInstance();
        try {
            db.collection("pratos")
                    .document(getIntent().getStringExtra("ID"))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot document) {
                            Prato c = new Prato();
                            c.setNome(document.get("nomeprato").toString());

                            dados = findViewById(R.id.dadosPrato);
                            dados.setText("Prato: " + c.getNome());

                            nome = findViewById(R.id.nomeprato);

                            nome.setText(c.getNome(), TextView.BufferType.EDITABLE);
                        }
                    });


        }catch(Throwable t){
            t.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Não foi possível carregar os dados  desse prato",
                    Toast.LENGTH_SHORT).show();
        }
        Button mButton = findViewById(R.id.atualizarPrato);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Prato c = new Prato();
                    c.setNome(nome.getText().toString());
                    if(!c.getNome().isEmpty()) {
                        db.collection("pratos")
                                .document(getIntent().getStringExtra("ID"))
                                .update("nomeprato", c.getNome()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),
                                        "O prato " + nome.getText().toString() + " foi atualizado com sucesso!",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AtualizarPrato.this, MainActivity.class);
                                intent.putExtra("idUsuarioLogado", getIntent().getStringExtra("idUsuarioLogado"));
                                startActivity(intent);
                            }
                        });



                    }
                    else{
                        throw new Exception();
                    }

                }catch (Throwable t){
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Não foi possível atualizar esse prato",
                            Toast.LENGTH_SHORT).show();
                }
    }

    });
        Button sButton = findViewById(R.id.removerPrato);
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db.collection("pratos")
                            .document(getIntent().getStringExtra("ID"))
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),
                                            "O prato foi deletado com sucesso!",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(AtualizarPrato.this, MainActivity.class);
                                    intent.putExtra("idUsuarioLogado", getIntent().getStringExtra("idUsuarioLogado"));
                                    startActivity(intent);
                                }
                            });

                    }
                catch (Throwable t){
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Não foi possível remover esse prato",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}

