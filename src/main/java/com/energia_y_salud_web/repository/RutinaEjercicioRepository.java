package com.energia_y_salud_web.repository;

import com.google.firebase.database.*;
import com.energia_y_salud_web.model.RutinaEjercicio;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Repository
public class RutinaEjercicioRepository {

    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("rutinaEjercicios");

    public List<RutinaEjercicio> findAll() {
        List<RutinaEjercicio> lista = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    RutinaEjercicio r = snap.getValue(RutinaEjercicio.class);
                    lista.add(r);
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
