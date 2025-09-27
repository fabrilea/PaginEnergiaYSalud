// RutinaEjercicioService.java
package com.energia_y_salud_web.service;


import com.energia_y_salud_web.model.RutinaEjercicio;
import com.google.firebase.database.*;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


@Service
public class RutinaEjercicioService {
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("rutinaEjercicios");


    public List<RutinaEjercicio> findAll() {
        List<RutinaEjercicio> lista = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    RutinaEjercicio r = snap.getValue(RutinaEjercicio.class);
                    if (r != null) {
                        r.setId(snap.getKey());
                        lista.add(r);
                    }
                }
                latch.countDown();
            }


            public void onCancelled(DatabaseError error) {
                latch.countDown();
            }
        });


        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        return lista;
    }
}