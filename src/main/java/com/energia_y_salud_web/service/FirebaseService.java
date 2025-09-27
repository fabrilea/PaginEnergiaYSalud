package com.energia_y_salud_web.service;

import com.google.firebase.database.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class FirebaseService {

    private final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public <T> CompletableFuture<List<T>> findAll(String collection, Class<T> clazz) {
        CompletableFuture<List<T>> future = new CompletableFuture<>();

        database.child(collection).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<T> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    T object = snapshot.getValue(clazz);
                    list.add(object);
                }
                future.complete(list);
            }

            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    public <T> CompletableFuture<T> findById(String collection, String id, Class<T> clazz) {
        CompletableFuture<T> future = new CompletableFuture<>();

        database.child(collection).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                T obj = snapshot.getValue(clazz);
                future.complete(obj);
            }

            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }

    public <T> void save(String collection, String id, T object) {
        database.child(collection).child(id).setValueAsync(object);
    }

    public void delete(String collection, String id) {
        database.child(collection).child(id).removeValueAsync();
    }
}
