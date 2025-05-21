package com.tingeso.descuento_por_personas_service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoPorPersonas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int numVueltas_TiempoMax; //número de vueltas o tiempo maximo (ej: 10 vueltas/15 minutos)
    private double tarifa; //precio base
    private int duracion_total;
}