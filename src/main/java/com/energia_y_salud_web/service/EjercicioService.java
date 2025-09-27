package com.energia_y_salud_web.service;

import com.energia_y_salud_web.model.Ejercicio;
import com.energia_y_salud_web.repository.EjercicioRepository;
import com.google.firebase.database.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class EjercicioService {

    private final DatabaseReference ejerciciosRef;

    public EjercicioService() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.ejerciciosRef = db.getReference("ejercicios"); // ✅ Colección correcta
    }

    public void save(Ejercicio ejercicio) {
        if (ejercicio.getId() == null || ejercicio.getId().isBlank()) {
            ejercicio.setId(ejerciciosRef.push().getKey());
        }
        ejerciciosRef.child(ejercicio.getId()).setValueAsync(ejercicio); // ✅ Se guarda en ejercicios
    }

    public List<Ejercicio> findAll() throws Exception {
        CompletableFuture<List<Ejercicio>> future = new CompletableFuture<>();
        ejerciciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Ejercicio> list = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Ejercicio e = ds.getValue(Ejercicio.class);
                    if (e != null) e.setId(ds.getKey());
                    list.add(e);
                }
                future.complete(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future.get();
    }

    public Ejercicio findById(String id) throws ExecutionException, InterruptedException {
        CompletableFuture<Ejercicio> future = new CompletableFuture<>();
        ejerciciosRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Ejercicio e = snapshot.getValue(Ejercicio.class);
                if (e != null) e.setId(snapshot.getKey());
                future.complete(e);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future.get();
    }

    public void delete(String id) {
        ejerciciosRef.child(id).removeValueAsync();
    }
}

