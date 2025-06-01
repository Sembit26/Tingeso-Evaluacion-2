package com.tingeso.reserva_service.Service;

import com.tingeso.reserva_service.Config.RestTemplateConfig;
import com.tingeso.reserva_service.Entity.Comprobante;
import com.tingeso.reserva_service.Entity.Reserva;
import com.tingeso.reserva_service.Model.DescuentoPorClienteFrecuente;
import com.tingeso.reserva_service.Model.DescuentoPorPersonas;
import com.tingeso.reserva_service.Model.TarifaDiasEspeciales;
import com.tingeso.reserva_service.Model.TarifaDuracion;
import com.tingeso.reserva_service.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private ComprobanteService comprobanteService;

    //----------------------------- CRUD -----------------------------
    //Obtener todas las reservas
    public List<Reserva> getAllReservas() {
        return reservaRepository.findAll();
    }

    //Obtener reserva por Id
    public Optional<Reserva> getReservaById(Integer id) {
        return reservaRepository.findById(id);
    }

    //crear Reserva
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

    //----------------- METODOS PARA OBTENER DATOS DE MICROSERVICIOS A OCUPAR ---------------------

    public TarifaDuracion obtenerTarifaNormal(int numVueltas_TiempoMaximo){
        TarifaDuracion tarifaDuracion = restTemplate.getForObject("http://tarifa-duracion-reserva-service/api/tarifasDuracion/buscarPorVueltas?numVueltas=" + numVueltas_TiempoMaximo, TarifaDuracion.class);
        return tarifaDuracion;
    }

    public DescuentoPorPersonas obtenerDescuentoPorCantidadDePersonas(int numPersonas){
        DescuentoPorPersonas descuentoPorPersonas = restTemplate.getForObject("http://descuento-por-personas-service/api/descuentoPorNumPersonas/buscarDescuento/" + numPersonas, DescuentoPorPersonas.class);
        return descuentoPorPersonas;
    }

    public DescuentoPorClienteFrecuente obtenerDescuentoPorFrecuenciaDeCliente(int numFrecuencia){
        DescuentoPorClienteFrecuente descuentoPorClienteFrecuente = restTemplate.getForObject("http://descuento-por-cliente-frecuente/api/descuentoPorClienteFrecuente/obtenerPorFrecuencia/" + numFrecuencia, DescuentoPorClienteFrecuente.class);
        return descuentoPorClienteFrecuente;
    }

    public TarifaDiasEspeciales obtenerTarifaParaDiasEspeciales(int numVueltas, int cantidadCumpleaneros){
        TarifaDiasEspeciales tarifaDiasEspeciales = restTemplate.getForObject("http://tarifa-dias-especiales/api/tarifasDiasEspeciales/buscarTarifaPorVueltasYPersonas?numVueltas=" + numVueltas + "&cantidadCumpleaneros=" + cantidadCumpleaneros, TarifaDiasEspeciales.class);
        return tarifaDiasEspeciales;
    }

    //----------------- LOGICA AVANZADA ---------------------
    //List<String> correosCumpleaneros,
    //String nombreCliente,
    //String correoCliente,
    //Map<String, String> nombreCorreo


    public Comprobante generarComprobante(double tarifa,
            double descuentoCumpleaneros,
            int maxCumpleanerosConDescuento,
            double descuentoPorCantidadDePersonas,
            double descuentoPorFrecuenciaCliente,
            String nombreCliente,
            String correoCliente,
            Map<String, String> nombreCorreo,
            List<String> correosCumpleaneros){
        Comprobante comprobante = comprobanteService.crearComprobante(tarifa,
            descuentoCumpleaneros,
            maxCumpleanerosConDescuento,
            descuentoPorCantidadDePersonas,
            descuentoPorFrecuenciaCliente,
            nombreCliente,
            correoCliente,
            nombreCorreo,
            correosCumpleaneros);
        return comprobante;
    }



}
