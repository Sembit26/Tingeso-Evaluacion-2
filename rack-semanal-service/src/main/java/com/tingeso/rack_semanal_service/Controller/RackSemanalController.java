package com.tingeso.rack_semanal_service.Controller;

import com.tingeso.rack_semanal_service.DTO.HorarioOcupadoDTO;
import com.tingeso.rack_semanal_service.Entity.RackSemanal;
import com.tingeso.rack_semanal_service.Service.RackSemanalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rackSemanal")
public class RackSemanalController {

    @Autowired
    private RackSemanalService rackSemanalService;

    @PostMapping("/create")
    public RackSemanal create(@RequestBody RackSemanal rackSemanal) {
        return rackSemanalService.save(rackSemanal);
    }

    @GetMapping
    public List<RackSemanal> getAll() {
        return rackSemanalService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<RackSemanal> getById(@PathVariable int id) {
        return rackSemanalService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        rackSemanalService.deleteById(id);
    }

    @GetMapping("/horariosOcupadosSemana")
    public List<HorarioOcupadoDTO> getHorariosOcupadosPorSemana(
            @RequestParam("inicio") String inicio,
            @RequestParam("fin") String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);

        return rackSemanalService.obtenerHorariosOcupadosPorSemana(fechaInicio, fechaFin);
    }
}
