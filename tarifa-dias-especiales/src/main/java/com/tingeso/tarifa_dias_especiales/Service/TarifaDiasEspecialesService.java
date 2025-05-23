package com.tingeso.tarifa_dias_especiales.Service;

import com.tingeso.tarifa_dias_especiales.Entity.TarifaDiasEspeciales;
import com.tingeso.tarifa_dias_especiales.Repository.TarifaDiasEspecialesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarifaDiasEspecialesService {

    @Autowired
    private TarifaDiasEspecialesRepository repository;

    public TarifaDiasEspeciales guardarTarifa(TarifaDiasEspeciales tarifa) {
        return repository.save(tarifa);
    }

    public List<TarifaDiasEspeciales> obtenerTodas() {
        return repository.findAll();
    }

    public Optional<TarifaDiasEspeciales> obtenerPorId(int id) {
        return repository.findById(id);
    }

    public List<TarifaDiasEspeciales> buscarPorNumVueltas(int numVueltas) {
        return repository.findByNumVueltasTiempoMax(numVueltas);
    }

    public Optional<TarifaDiasEspeciales> obtenerTarifaPorVueltasYPersonas(int numVueltas, int cantidadPersonas) {
        List<TarifaDiasEspeciales> tarifas = repository.findByNumVueltasTiempoMax(numVueltas);

        return tarifas.stream()
                .filter(t -> cantidadPersonas >= t.getMinPersonas() && cantidadPersonas <= t.getMaxPersonas())
                .findFirst(); // o usar lógica más compleja si hay más de una válida
    }


    public TarifaDiasEspeciales actualizarTarifa(int id, TarifaDiasEspeciales nuevaTarifa) {
        Optional<TarifaDiasEspeciales> existente = repository.findById(id);
        if (existente.isPresent()) {
            TarifaDiasEspeciales tarifa = existente.get();
            tarifa.setNumVueltasTiempoMax(nuevaTarifa.getNumVueltasTiempoMax());
            tarifa.setDuracion_total(nuevaTarifa.getDuracion_total());
            tarifa.setTarifa(nuevaTarifa.getTarifa());
            tarifa.setMinPersonas(nuevaTarifa.getMinPersonas());
            tarifa.setMaxPersonas(nuevaTarifa.getMaxPersonas());
            tarifa.setMaxCumpleanerosConDescuento(nuevaTarifa.getMaxCumpleanerosConDescuento());
            tarifa.setDescuentoCumpleaneros(nuevaTarifa.getDescuentoCumpleaneros());
            return repository.save(tarifa);
        } else {
            return null;
        }
    }

    public boolean eliminarTarifa(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
