package com.tingeso.reserva_service.Controller;

import com.tingeso.reserva_service.Entity.Reserva;
import com.tingeso.reserva_service.Service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        return ResponseEntity.ok(reservaService.getAllReservas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Integer id) {
        return reservaService.getReservaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reserva> createReserva(@RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaService.createReserva(reserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        Reserva updated = reservaService.updateReserva(id, reserva);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Integer id) {
        reservaService.deleteReserva(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/obtenerTarifa")
    public double getTarifa(@RequestParam int numVueltas) {
        return reservaService.crearReserva(numVueltas);
    }
}
