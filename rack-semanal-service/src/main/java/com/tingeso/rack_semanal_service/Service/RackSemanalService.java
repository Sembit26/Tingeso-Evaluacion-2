package com.tingeso.rack_semanal_service.Service;

import com.tingeso.rack_semanal_service.DTO.HorarioOcupadoDTO;
import com.tingeso.rack_semanal_service.DTO.ReservaDTO;
import com.tingeso.rack_semanal_service.Entity.RackSemanal;
import com.tingeso.rack_semanal_service.Repository.RackSemanalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RackSemanalService {

    @Autowired
    private RackSemanalRepository rackSemanalRepository;

    @Autowired
    RestTemplate restTemplate;

    public RackSemanal save(RackSemanal rackSemanal) {
        return rackSemanalRepository.save(rackSemanal);
    }

    public List<RackSemanal> findAll() {
        return rackSemanalRepository.findAll();
    }

    public Optional<RackSemanal> findById(int id) {
        return rackSemanalRepository.findById(id);
    }

    public void deleteById(int id) {
        rackSemanalRepository.deleteById(id);
    }

    public List<HorarioOcupadoDTO> obtenerHorariosOcupadosPorSemana(LocalDate semanaInicio, LocalDate semanaFin) {
        // Llamar al microservicio de reserva (ajusta la URL según tu servicio)
        String url = "http://reserva-service/api/reservas/getAll";
        ReservaDTO[] reservas = restTemplate.getForObject(url, ReservaDTO[].class);

        // Verificación por si no se pudo obtener reservas
        if (reservas == null) {
            return Collections.emptyList();
        }

        // Filtrar solo las reservas dentro de la semana solicitada
        List<ReservaDTO> reservasSemana = Arrays.stream(reservas)
                .filter(r -> !r.getFechaInicio().isBefore(semanaInicio) && !r.getFechaInicio().isAfter(semanaFin))
                .collect(Collectors.toList());

        // Agrupar por fecha y formar los bloques ocupados
        Map<LocalDate, List<String>> mapaHorarios = new HashMap<>();

        for (ReservaDTO reserva : reservasSemana) {
            String horario = reserva.getHoraInicio() + " - " + reserva.getHoraFin();
            mapaHorarios.computeIfAbsent(reserva.getFechaInicio(), k -> new ArrayList<>()).add(horario);
        }

        // Convertir el mapa a la lista de DTOs
        List<HorarioOcupadoDTO> resultado = new ArrayList<>();
        for (Map.Entry<LocalDate, List<String>> entry : mapaHorarios.entrySet()) {
            resultado.add(new HorarioOcupadoDTO(entry.getKey(), entry.getValue()));
        }

        // Orden opcional por fecha
        resultado.sort(Comparator.comparing(HorarioOcupadoDTO::getFecha));
        return resultado;
    }
}
