package com.tingeso.reportes_service.Service;

import com.tingeso.reportes_service.DTO.ReservaDTO;
import com.tingeso.reportes_service.Entity.Reportes;
import com.tingeso.reportes_service.Repository.ReportesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportesService {

    @Autowired
    private ReportesRepository reportesRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Reportes> obtenerTodos() {
        return reportesRepository.findAll();
    }

    public Optional<Reportes> obtenerPorId(Long id) {
        return reportesRepository.findById(id);
    }

    public Reportes guardar(Reportes reporte) {
        return reportesRepository.save(reporte);
    }

    public void eliminar(Long id) {
        reportesRepository.deleteById(id);
    }

    public List<ReservaDTO> obtenerReservasPorRangoDeMeses(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        // Ajustar fechas al primer día del mes y al último día del mes
        LocalDate inicioMes = fechaInicio.withDayOfMonth(1);
        LocalDate finMes = fechaFin.withDayOfMonth(fechaFin.lengthOfMonth());

        // Llamar al microservicio de reservas
        String url = "http://reserva-service/api/reservas/getAll";
        ResponseEntity<ReservaDTO[]> response = restTemplate.getForEntity(url, ReservaDTO[].class);
        ReservaDTO[] reservasArray = response.getBody();

        if (reservasArray == null) {
            return Collections.emptyList();
        }

        // Filtrar las reservas que están dentro del rango de fechas
        return Arrays.stream(reservasArray)
                .filter(reserva -> !reserva.getFechaInicio().isBefore(inicioMes) && !reserva.getFechaInicio().isAfter(finMes))
                .collect(Collectors.toList());
    }

}
