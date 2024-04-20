package com.example.examenpm01p3scsh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.examenpm01p3scsh.configuracion.entrevista;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class modelentrevista extends AppCompatActivity {

    private RecyclerView recyclerView;
    private adaptador entrevistaAdapter;
    private List<Entrevista> listaEntrevistas;
    Button btnRegresar, btnEliminar, btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modelentrevista);
        btnRegresar = findViewById(R.id.btnRegresar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar =  findViewById(R.id.btnEliminar);
        recyclerView = findViewById(R.id.recyclerViewEntrevistas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnEliminar.setOnClickListener(v -> {
            entrevistaAdapter.notifyDataSetChanged();

            List<String> idsSeleccionados = entrevistaAdapter.obtenerIdsSeleccionados();

            eliminarDocumentosFirestore(idsSeleccionados);
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(modelentrevista.this, MainActivity.class);
                startActivity(intent);
            }
        });

        obtenerListaEntrevistas();

        entrevistaAdapter = new adaptador(this, listaEntrevistas);
        recyclerView.setAdapter(entrevistaAdapter);
    }

    private void obtenerListaEntrevistas() {
        listaEntrevistas = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Entrevista")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String idFirestore = document.getId();
                            Entrevista entrevista = document.toObject(Entrevista.class);
                            entrevista.setIdFirestore(idFirestore);
                            listaEntrevistas.add(entrevista);
                        }
                        actualizarRecyclerView();
                    } else {
                        if (!listaEntrevistas.isEmpty()) {
                            entrevistaAdapter = new adaptador(this, listaEntrevistas);
                            recyclerView.setAdapter(entrevistaAdapter);

                            RecyclerView.Adapter adapter = recyclerView.getAdapter();
                            if (adapter instanceof adaptador) {
                                entrevistaAdapter = (adaptador) adapter;
                            }
                        } else {
                            Toast.makeText(modelentrevista.this, "No hay datos disponibles", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void actualizarRecyclerView() {
        // Verifica si la lista no está vacía y actualiza el adaptador
        if (!listaEntrevistas.isEmpty()) {
            entrevistaAdapter = new adaptador(this, listaEntrevistas);
            recyclerView.setAdapter(entrevistaAdapter);
        } else {
            Toast.makeText(modelentrevista.this, "No hay datos disponibles", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarDocumentosFirestore(List<String> idsFirestore) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (String id : idsFirestore) {
            db.collection("Entrevista")
                    .document(id)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        entrevistaAdapter.actualizarListaDespuesEliminar(idsFirestore);
                        Toast.makeText(modelentrevista.this, "Eliminado de firebase ", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(modelentrevista.this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
