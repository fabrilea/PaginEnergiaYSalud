package com.energia_y_salud_web.service;

import com.google.firebase.database.*;
import com.energia_y_salud_web.model.UsuarioRutina;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class UsuarioRutinaService {

    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarioRutinas");

    public List<UsuarioRutina> obtenerTodos() {
        List<UsuarioRutina> lista = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    UsuarioRutina ur = snap.getValue(UsuarioRutina.class);
                    lista.add(ur);
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
