package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDto;
import ar.edu.utn.frc.tup.lciii.services.TelemetryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/telemetry")
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class TelemetryController {
    @Autowired
    private TelemetryService telemetryService;
    //POST /telemetry
    //Recibe un JSON con los datos de telemetría.
    //Valida que el hostname exista en la tabla Device, de lo contrario, rechazar la solicitud.
    //Guarda los datos en la base de datos.
    //Retorna un 201 Created si la operación fue exitosa.
    @PostMapping("")
    private ResponseEntity<TelemetryDto> postTelemetry(@RequestBody TelemetryDto telemetryDto){
            return ResponseEntity.ok(telemetryService.postTelemetry(telemetryDto));
    }

    //Devuelve una lista con todas las entradas de telemetría registradas.
    //GET /telemetry
    @GetMapping("")
    ResponseEntity<List<TelemetryDto>> getAllTelemetry(@RequestParam(required = false) String hostname){
        return ResponseEntity.ok(telemetryService.getAllTelemetry(hostname));
    }
}