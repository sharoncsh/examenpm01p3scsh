package com.example.examenpm01p3scsh;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.examenpm01p3scsh.configuracion.entrevista;
import com.example.examenpm01p3scsh.configuracion.utils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class audioentrevista extends AppCompatActivity {

    private RecyclerView recyclerView;
    private audio Escuchar_audio_adapter;
    private List<Entrevista> listaEntrevistas;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listentrevista);

        btnRegresar = findViewById(R.id.btnRegresar);
        recyclerView = findViewById(R.id.recyclerViewEntrevistas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(audioentrevista.this, MainActivity.class);
                startActivity(intent);
            }
        });

        obtenerListaEntrevistas();

        audio = new audio(this, listaEntrevistas);
        recyclerView.setAdapter(audio);
    }

    private void obtenerListaEntrevistas() {
        listaEntrevistas = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Entrevista")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Entrevista entrevista = document.toObject(Entrevista.class);
                            listaEntrevistas.add(entrevista);
                        }
                        actualizarRecyclerView();
                    } else {
                        Toast.makeText(audioentrevista.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void actualizarRecyclerView() {
        if (!listaEntrevistas.isEmpty()) {
            if (audio == null) {
                audio = new audio(this, listaEntrevistas);
                recyclerView.setAdapter(audio);
            } else {
                Escuchar_audio_adapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(audioentrevista.this, "No hay datos disponibles", Toast.LENGTH_SHORT).show();
        }
    }
}