package com.energia_y_salud_web.service;

import com.energia_y_salud_web.dto.UsuarioConRutinas;
import com.energia_y_salud_web.model.Rutina;
import com.energia_y_salud_web.model.RutinaEjercicio;
import com.energia_y_salud_web.model.Usuario;
import com.google.firebase.database.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class UsuarioService {

    private final DatabaseReference usuariosRef;
    private final DatabaseReference usuarioRutinasRef;
    private final DatabaseReference rutinasRef;
    private final RutinaService rutinaService;

    public UsuarioService(RutinaService rutinaService) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.usuariosRef = db.getReference("usuarios");
        this.usuarioRutinasRef = db.getReference("usuarioRutinas");
        this.rutinasRef = db.getReference("rutinas");
        this.rutinaService = rutinaService;
    }

    // 👉 Guardar usuario
    // 👉 guardar usuario
    public void save(Usuario usuario) {
        usuariosRef.child(usuario.getDni()).setValueAsync(usuario);
    }

    // 👉 buscar usuario
    public CompletableFuture<Usuario> buscarPorDni(String dni) {
        CompletableFuture<Usuario> future = new CompletableFuture<>();
        usuariosRef.child(dni).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Usuario u = snapshot.getValue(Usuario.class);
                if (u != null) u.setDni(snapshot.getKey());
                future.complete(u);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future;
    }

    // 👉 listar usuarios
    public CompletableFuture<List<Usuario>> findAll() {
        CompletableFuture<List<Usuario>> future = new CompletableFuture<>();
        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Usuario> list = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Usuario u = ds.getValue(Usuario.class);
                    if (u != null) {
                        u.setDni(ds.getKey());
                        list.add(u);
                    }
                }
                future.complete(list);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future;
    }

    // 👉 eliminar usuario
    public void deleteByDni(String dni) {
        usuariosRef.child(dni).removeValueAsync();
    }

    // 👉 Asignar rutina (usa lista, no mapa)
    public CompletableFuture<Void> asignarRutina(String dni, String rutinaId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        DatabaseReference ref = usuariosRef.child(dni).child("usuarioRutinas");

        rutinasRef.child(rutinaId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Rutina rutina = snapshot.getValue(Rutina.class);
                if (rutina == null) {
                    future.complete(null);
                    return;
                }
                rutina.setId(rutinaId);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        List<Rutina> lista = new ArrayList<>();
                        for (DataSnapshot child : snap.getChildren()) {
                            Rutina r = child.getValue(Rutina.class);
                            if (r != null) lista.add(r);
                        }

                        // evitar duplicados
                        boolean yaTiene = lista.stream()
                                .anyMatch(r -> r.getId().equals(rutinaId));
                        if (!yaTiene) lista.add(rutina);

                        // ⚡ usar callback para saber cuándo terminó
                        ref.setValue(lista, (error, ref2) -> {
                            if (error != null) {
                                future.completeExceptionally(error.toException());
                            } else {
                                future.complete(null);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }





    public void quitarRutina(String dni, String rutinaId) {
        DatabaseReference ref = usuariosRef.child(dni).child("usuarioRutinas");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Rutina> nuevas = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Rutina r = child.getValue(Rutina.class);
                    if (r != null && !r.getId().equals(rutinaId)) {
                        nuevas.add(r);
                    }
                }
                ref.setValueAsync(nuevas);
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }



    // 👉 Buscar usuario con sus rutinas (DTO)
    public CompletableFuture<UsuarioConRutinas> buscarUsuarioConRutinas(String dni) {
        CompletableFuture<UsuarioConRutinas> future = new CompletableFuture<>();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(dni);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    future.complete(null);
                    return;
                }

                Usuario usuario = snapshot.getValue(Usuario.class);

                // Leer rutinas (pueden estar como lista o mapa)
                List<Rutina> rutinas = new ArrayList<>();
                DataSnapshot rutinasSnap = snapshot.child("usuarioRutinas");
                if (rutinasSnap.exists()) {
                    for (DataSnapshot child : rutinasSnap.getChildren()) {
                        Rutina r = child.getValue(Rutina.class);
                        if (r != null) rutinas.add(r);
                    }
                }

                future.complete(new UsuarioConRutinas(usuario, rutinas));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException("Error al leer usuario: " + error.getMessage()));
            }
        });

        return future;
    }
}
