import React, { useEffect, useState } from "react";
import { Calendar, dateFnsLocalizer } from "react-big-calendar";
import { parse, startOfWeek, format, getDay } from "date-fns";
import 'react-big-calendar/lib/css/react-big-calendar.css';
import rackSemanalService from "../services/rackSemanal.service";

import { useNavigate } from "react-router-dom";
import es from 'date-fns/locale/es';

const locales = { es };

const localizer = dateFnsLocalizer({
  format,
  parse,
  startOfWeek: () => startOfWeek(new Date(), { weekStartsOn: 1 }),
  getDay,
  locales,
});

const ViewReservationsCalendar = () => {
  const [eventos, setEventos] = useState([]);
  const [currentDate, setCurrentDate] = useState(new Date()); // Track the current date being viewed
  const navigate = useNavigate();

  useEffect(() => {
    //const cliente = sessionStorage.getItem('cliente');
    const now = new Date();
    now.setSeconds(0);
    now.setMilliseconds(0);

    rackSemanalService.horariosDisponiblesSeisMeses()
      .then((res) => {
        const data = res.data;
        const eventosGenerados = [];

        data.forEach(({ fecha, horariosDisponibles }) => {
        horariosDisponibles.forEach(bloque => {
          const [inicioStr, finStr] = bloque.split(' - ');
          const [anio, mes, dia] = fecha.split('-').map(Number);
          const [hIni, mIni] = inicioStr.split(':').map(Number);
          const [hFin, mFin] = finStr.split(':').map(Number);

          let start = new Date(anio, mes - 1, dia, hIni, mIni);
          const end = new Date(anio, mes - 1, dia, hFin, mFin);

          const esHoy = start.toDateString() === now.toDateString();

          if (esHoy && start <= now) {
            start = new Date(now); // Ajustar si ya pasó
          }

          const minutosDisponibles = (end - start) / 60000;

          if (end > now && minutosDisponibles >= 30) {
            eventosGenerados.push({
              title: 'Disponible',
              start,
              end,
              allDay: false,
            });
          }
        });
      });


        setEventos(eventosGenerados);
      })
      .catch((err) => {
        console.error("Error al obtener horarios:", err);
      });
  }, [navigate, currentDate]); // Re-run when the currentDate changes

  const handleNavigate = (date) => {
    // This function is triggered when the user navigates through the calendar
    setCurrentDate(date); // Update the current date being viewed
  };

  return (
    <div className="calendar-page">
      <h2 className="calendar-title">Horarios Disponibles de la Semana</h2>

      <div className="calendar-wrapper">
        <Calendar
          localizer={localizer}
          events={eventos}
          startAccessor="start"
          endAccessor="end"
          views={['week']}
          defaultView="week"
          step={20}
          timeslots={2}
          min={new Date(1970, 1, 1, 10, 0)}
          max={new Date(1970, 1, 1, 22, 0)}
          style={{ height: '75vh', width: '100%' }}
          onSelectEvent={(event) => {
            const fecha = format(event.start, 'yyyy-MM-dd');
            const hora = format(event.start, 'HH:mm');
            navigate('/generarReserva', {
              state: { fecha, hora },
            });
          }}
          messages={{
            week: 'Semana',
            day: 'Día',
            today: 'Hoy',
            previous: 'Anterior',
            next: 'Siguiente',
          }}
          date={currentDate} // Set current date for the calendar
          onNavigate={handleNavigate} // This will handle navigation (previous, next, today)
        />
      </div>
    </div>
  );
};

export default ViewReservationsCalendar;
