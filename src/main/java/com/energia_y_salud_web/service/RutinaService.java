package com.energia_y_salud_web.service;

import com.energia_y_salud_web.model.Rutina;
import com.energia_y_salud_web.model.Historial;
import com.google.firebase.database.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RutinaService {

    private final DatabaseReference rutinasRef;
    private final DatabaseReference ejerciciosRef;
    private final DatabaseReference historialRef;

    public RutinaService() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.rutinasRef = db.getReference("rutinas");
        this.ejerciciosRef = db.getReference("ejercicios");
        this.historialRef = db.getReference("historial");
    }

    // 👉 Guardar rutina completa (con lista de ejercicios)
    public void save(Rutina rutina) {
        if (rutina.getId() == null || rutina.getId().isBlank()) {
            rutina.setId(rutinasRef.push().getKey());
        }
        rutinasRef.child(rutina.getId()).setValueAsync(rutina);
    }

    // 👉 listar todas las rutinas
    public CompletableFuture<List<Rutina>> findAll() {
        CompletableFuture<List<Rutina>> future = new CompletableFuture<>();
        rutinasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Rutina> list = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Rutina r = ds.getValue(Rutina.class);
                    if (r != null) {
                        r.setId(ds.getKey());
                        list.add(r);
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

    // 👉 buscar por id
    public CompletableFuture<Rutina> findById(String id) {
        CompletableFuture<Rutina> future = new CompletableFuture<>();
        rutinasRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Rutina r = snapshot.getValue(Rutina.class);
                if (r != null) r.setId(snapshot.getKey());
                future.complete(r);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future;
    }

    // 👉 eliminar rutina
    public void delete(String id) {
        rutinasRef.child(id).removeValueAsync();
    }

    // 👉 registrar historial (ahora también con nombre del ejercicio)
    public void registrarHistorial(String dni, String rutinaId, String ejercicioId,
                                   double pesoUsado, int reps) {
        DatabaseReference ref = historialRef.child(dni).child(rutinaId);

        String key = ref.push().getKey();
        Historial registro = new Historial();
        registro.setId(key);
        registro.setUsuarioId(dni);
        registro.setRutinaId(rutinaId);
        registro.setEjercicioId(ejercicioId);
        registro.setPesoUsado(pesoUsado);
        registro.setRepeticionesRealizadas(reps);
        registro.setFecha(LocalDate.now().toString());

        // Traer nombre del ejercicio y guardar
        ejerciciosRef.child(ejercicioId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                var ej = snapshot.getValue(com.energia_y_salud_web.model.Ejercicio.class);
                if (ej != null) {
                    registro.setEjercicioNombre(ej.getNombre());
                }
                ref.child(key).setValueAsync(registro);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                ref.child(key).setValueAsync(registro); // fallback
            }
        });
    }

    // 👉 obtener historial de un usuario y rutina
    public CompletableFuture<List<Historial>> obtenerHistorial(String dni, String rutinaId) {
        CompletableFuture<List<Historial>> future = new CompletableFuture<>();
        DatabaseReference ref = historialRef.child(dni).child(rutinaId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Historial> historial = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Historial h = snap.getValue(Historial.class);
                    if (h != null) {
                        historial.add(h);
                    }
                }
                // Ordenar por fecha descendente
                historial.sort((a, b) -> b.getFecha().compareTo(a.getFecha()));
                // Limitar a 5
                if (historial.size() > 5) {
                    historial = historial.subList(0, 5);
                }
                future.complete(historial);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }
}
