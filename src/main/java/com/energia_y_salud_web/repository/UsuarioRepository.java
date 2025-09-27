package com.energia_y_salud_web.repository;

import com.energia_y_salud_web.model.Usuario;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class UsuarioRepository {

    private final DatabaseReference usuariosRef;

    public UsuarioRepository(DatabaseReference usuariosRef) {
        this.usuariosRef = usuariosRef;
    }

    // CREATE / UPDATE
    public void save(Usuario usuario) {
        usuariosRef.child(usuario.getDni()).setValueAsync(usuario);
    }

    // READ by dni
    public CompletableFuture<Usuario> findByDni(String dni) {
        CompletableFuture<Usuario> future = new CompletableFuture<>();
        usuariosRef.child(dni).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Usuario u = snapshot.getValue(Usuario.class);
                future.complete(u);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future;
    }

    // READ all
    public CompletableFuture<List<Usuario>> findAll() {
        CompletableFuture<List<Usuario>> future = new CompletableFuture<>();
        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Usuario> lista = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Usuario u = child.getValue(Usuario.class);
                    lista.add(u);
                }
                future.complete(lista);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future;
    }

    // DELETE
    public void deleteByDni(String dni) {
        usuariosRef.child(dni).removeValueAsync();
    }
}
