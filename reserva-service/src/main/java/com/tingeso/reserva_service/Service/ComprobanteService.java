package com.tingeso.reserva_service.Service;

import com.tingeso.reserva_service.Entity.Comprobante;
import com.tingeso.reserva_service.Entity.DetallePagoPorPersona;
import com.tingeso.reserva_service.Repository.ComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ComprobanteService {

    @Autowired
    private ComprobanteRepository comprobanteRepository;

    public List<Comprobante> getAll() {
        return comprobanteRepository.findAll();
    }

    public Comprobante getById(Long id) {
        return comprobanteRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        comprobanteRepository.deleteById(id);
    }

    //------------------ LOGICA GENERAR COMPROBANTE ---------------------

    public Comprobante crearComprobante(
            double tarifa,
            double descuentoCumpleaneros,
            int maxCumpleanerosConDescuento,
            double descuentoPorCantidadDePersonas,
            double descuentoPorFrecuenciaCliente,
            String nombreCliente,
            String correoCliente,
            Map<String, String> nombreCorreo,
            List<String> correosCumpleaneros
    ) {
        double totalSinIva = 0.0;
        int cumpleDescuentoAsignado = 0;
        List<DetallePagoPorPersona> detalles = new ArrayList<>();

        // --- Procesar grupo completo (incluyendo cliente principal) ---
        for (Map.Entry<String, String> entry : nombreCorreo.entrySet()) {
            String nombre = entry.getKey();
            String correo = entry.getValue();

            boolean esClientePrincipal = nombre.equals(nombreCliente) && correo.equals(correoCliente);
            double descuentoAplicado = 0.0;
            String tipoDescuento = "Ninguno";

            // --- Lógica de descuentos en orden de prioridad ---
            if (correosCumpleaneros.contains(correo) && cumpleDescuentoAsignado < maxCumpleanerosConDescuento) {
                descuentoAplicado = descuentoCumpleaneros;
                tipoDescuento = "Cumpleaños";
                cumpleDescuentoAsignado++;
            } else if (esClientePrincipal && descuentoPorFrecuenciaCliente > 0) {
                descuentoAplicado = descuentoPorFrecuenciaCliente;
                tipoDescuento = "Frecuencia";
            } else if (!esClientePrincipal && descuentoPorCantidadDePersonas > 0) {
                descuentoAplicado = descuentoPorCantidadDePersonas;
                tipoDescuento = "Grupal";
            }

            // --- Cálculo de montos ---
            double montoSinIva = tarifa * (1 - descuentoAplicado);
            double iva = montoSinIva * 0.19;
            double totalConIva = montoSinIva + iva;
            totalSinIva += montoSinIva;

            DetallePagoPorPersona detalle = new DetallePagoPorPersona();
            detalle.setNombrePersona(nombre);
            detalle.setPrecioBase(tarifa);
            detalle.setTipoDescuento(tipoDescuento);
            detalle.setPorcentajeDescuento(Math.round(descuentoAplicado * 100.0)); // porcentaje entero
            detalle.setMontoFinalSinIva(Math.round(montoSinIva * 100.0) / 100.0);
            detalle.setIva(Math.round(iva * 100.0) / 100.0);
            detalle.setTotalConIva(Math.round(totalConIva * 100.0) / 100.0);

            detalles.add(detalle);
        }

        double ivaTotal = totalSinIva * 0.19;
        double totalConIva = totalSinIva + ivaTotal;

        Comprobante comprobante = new Comprobante();
        comprobante.setPrecio_final(Math.round(totalSinIva * 100.0) / 100.0);
        comprobante.setIva(Math.round(ivaTotal * 100.0) / 100.0);
        comprobante.setMonto_total_iva(Math.round(totalConIva * 100.0) / 100.0);
        comprobante.setDetallesPago(detalles);

        return comprobanteRepository.save(comprobante);
    }

}
