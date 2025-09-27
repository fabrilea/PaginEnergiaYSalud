package com.energia_y_salud_web.repository;

import com.energia_y_salud_web.model.Rutina;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class RutinaRepository {

    private final DatabaseReference rutinasRef;

    public RutinaRepository(DatabaseReference firebaseDatabase) {
        this.rutinasRef = firebaseDatabase.child("rutinas");
    }

    public CompletableFuture<List<Rutina>> findAll() {
        CompletableFuture<List<Rutina>> future = new CompletableFuture<>();
        rutinasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Rutina> result = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Rutina r = child.getValue(Rutina.class);
                    if (r != null) result.add(r);
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

    public void save(Rutina rutina) {
        if (rutina.getId() == null || rutina.getId().isEmpty()) {
            rutina.setId(rutinasRef.push().getKey());
        }
        rutinasRef.child(rutina.getId()).setValueAsync(rutina);
    }

    public CompletableFuture<Rutina> findById(String id) {
        CompletableFuture<Rutina> future = new CompletableFuture<>();
        rutinasRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                future.complete(snapshot.getValue(Rutina.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future;
    }

    public void delete(String id) {
        rutinasRef.child(id).removeValueAsync();
    }
}
