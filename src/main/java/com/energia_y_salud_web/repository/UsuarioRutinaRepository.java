package com.energia_y_salud_web.repository;

import com.energia_y_salud_web.model.UsuarioRutina;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class UsuarioRutinaRepository {

    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarioRutinas");

    public CompletableFuture<List<UsuarioRutina>> findByUsuarioId(String usuarioId) {
        CompletableFuture<List<UsuarioRutina>> future = new CompletableFuture<>();
        ref.orderByChild("usuarioId").equalTo(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                List<UsuarioRutina> list = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    UsuarioRutina ur = snap.getValue(UsuarioRutina.class);
                    if (ur != null) {
                        ur.setId(snap.getKey());
                        list.add(ur);
                    }
                }
                future.complete(list);
            }

            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future;
    }
}
