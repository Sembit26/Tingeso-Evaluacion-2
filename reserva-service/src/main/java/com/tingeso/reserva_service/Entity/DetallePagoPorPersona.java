package com.tingeso.reserva_service.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "detalle_pago_persona")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePagoPorPersona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombrePersona;

    private double precioBase;             // Precio sin IVA
    private String tipoDescuento;          // Ej: "Grupal", "Cumpleaños", "Ninguno"
    private double porcentajeDescuento;    // Ej: 10 o 50
    private double montoFinalSinIva;       // Precio luego del descuento, sin IVA
    private double iva;                    // IVA sobre el monto final sin IVA
    private double totalConIva;            // Precio final con IVA
}
