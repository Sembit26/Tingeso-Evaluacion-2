package com.tingeso.reserva_service.Service;

import com.tingeso.reserva_service.Config.RestTemplateConfig;
import com.tingeso.reserva_service.Entity.Reserva;
import com.tingeso.reserva_service.Model.TarifaDuracion;
import com.tingeso.reserva_service.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    RestTemplate restTemplate;

    public List<Reserva> getAllReservas() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> getReservaById(Integer id) {
        return reservaRepository.findById(id);
    }

    public Reserva createReserva(Reserva reserva) {
        // gracias a CascadeType.ALL en Reserva, los DetallePagoPorPersona también se guardarán
        return reservaRepository.save(reserva);
    }

    public Reserva updateReserva(Integer id, Reserva reservaActualizada) {
        return reservaRepository.findById(id).map(reservaExistente -> {
            reservaActualizada.setId(reservaExistente.getId());
            return reservaRepository.save(reservaActualizada);
        }).orElse(null);
    }

    public void deleteReserva(Integer id) {
        reservaRepository.deleteById(id);
    }

    public TarifaDuracion obtenerTarifaNormal(int numVueltas_TiempoMaximo){
        TarifaDuracion tarifaDuracion = restTemplate.getForObject("http://tarifa-duracion-reserva-service/api/tarifasDuracion/buscarPorVueltas?numVueltas=" + numVueltas_TiempoMaximo, TarifaDuracion.class);
        return tarifaDuracion;
    }
}
