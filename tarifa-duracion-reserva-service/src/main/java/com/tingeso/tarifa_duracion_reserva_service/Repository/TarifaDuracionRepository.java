package com.tingeso.tarifa_duracion_reserva_service.Repository;

import com.tingeso.tarifa_duracion_reserva_service.Entity.TarifaDuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifaDuracionRepository extends JpaRepository<TarifaDuracion, Integer> {
    // Puedes agregar métodos custom si los necesitas
}
