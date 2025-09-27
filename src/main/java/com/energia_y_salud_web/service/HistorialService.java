// HistorialService.java
package com.energia_y_salud_web.service;


import com.energia_y_salud_web.model.Historial;
import com.google.firebase.database.*;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


@Service
public class HistorialService {
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("historiales");


    public List<Historial> findAll() {
        List<Historial> lista = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Historial h = snap.getValue(Historial.class);
                    if (h != null) {
                        h.setId(snap.getKey());
                        lista.add(h);
                    }
                }
                latch.countDown();
            }


            @Override
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