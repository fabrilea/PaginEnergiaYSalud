package com.energia_y_salud_web.repository;

import com.energia_y_salud_web.model.Ejercicio;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class EjercicioRepository {

    private final DatabaseReference ejerciciosRef;

    public EjercicioRepository(DatabaseReference firebaseDatabase) {
        this.ejerciciosRef = firebaseDatabase.child("ejercicios");
    }

    public CompletableFuture<List<Ejercicio>> findAll() {
        CompletableFuture<List<Ejercicio>> future = new CompletableFuture<>();
        ejerciciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Ejercicio> result = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Ejercicio e = child.getValue(Ejercicio.class);
                    if (e != null) {
                        e.setId(child.getKey());
                        result.add(e);
                    }
                }
                future.complete(result);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future;
    }

    public CompletableFuture<Ejercicio> findById(String id) {
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
        return future;
    }

    public void save(Ejercicio ejercicio) {
        if (ejercicio.getId() == null || ejercicio.getId().isEmpty()) {
            ejercicio.setId(ejerciciosRef.push().getKey());
        }
        ejerciciosRef.child(ejercicio.getId()).setValueAsync(ejercicio);
    }

    public void delete(String id) {
        ejerciciosRef.child(id).removeValueAsync();
    }
}
