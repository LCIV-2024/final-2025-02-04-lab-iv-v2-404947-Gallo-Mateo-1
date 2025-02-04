package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDto;

import java.util.List;

public interface TelemetryService {
    //POST /telemetry
    //Recibe un JSON con los datos de telemetría.
    //Valida que el hostname exista en la tabla Device, de lo contrario, rechazar la solicitud.
    //Guarda los datos en la base de datos.
    //Retorna un 201 Created si la operación fue exitosa.
    TelemetryDto postTelemetry(TelemetryDto telemetryDto);

    //Devuelve una lista con todas las entradas de telemetría registradas.
    //GET /telemetry
    List<TelemetryDto> getAllTelemetry();

}
