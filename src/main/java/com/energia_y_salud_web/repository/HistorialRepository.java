package com.energia_y_salud_web.repository;

import com.google.firebase.database.*;
import com.energia_y_salud_web.model.Historial;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Repository
public class HistorialRepository {

    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("historiales");

    public List<Historial> findAll() {
        List<Historial> lista = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Historial h = snap.getValue(Historial.class);
                    lista.add(h);
                }
                latch.countDown();
            }
            public void onCancelled(DatabaseError error) {
                latch.countDown();
            }
        });

        try { latch.await(); } catch (InterruptedException e) { e.printStackTrace(); }
        return lista;
    }
}
