package com.tingeso.reserva_service.Entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "reserva_id")  // clave foránea en DetallePagoPorPersona
    private List<DetallePagoPorPersona> detallesPago;

    private int idUsuario;

    private int numVueltasTiempoMaximo; //(10, 15 o 20 minutos o vueltas)
    private int numPersonas; //Cantidad de personas para las que se generó la reserva
    private int precioRegular; //Precio base
    private int duracionTotal; //Duracion maxima (3, 35 o 40 min)
    private LocalDateTime fechaHora; // Fecha en la que se generó la reserva
    private String nombreCliente;

    private LocalDate fechaInicio; //fecha de inicio de reserva
    private LocalTime horaInicio; // hora de inicio de reserva
    private LocalTime horaFin; // hora de fin de la reserva

    private double descuento; // descuento total aplicado al grupo
    private double precioFinal; // precio final del grupo (total)
    private double iva; // valor del IVA total
    private double montoTotalIva; // precio total con IVA

}
