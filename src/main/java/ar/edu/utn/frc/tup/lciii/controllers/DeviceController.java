package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDto;
import ar.edu.utn.frc.tup.lciii.model.DeviceType;
import ar.edu.utn.frc.tup.lciii.services.DeviceService;
import ar.edu.utn.frc.tup.lciii.services.TelemetryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/device")
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    //POST /device
    //Recibe un JSON con los datos del dispositivo.
    //Valida que el hostname no exista preeviamente en la tabla Device, de lo contrario, rechazar la solicitud.
    //Guarda los datos en la base de datos.
    //Retorna un 201 Created si la operación fue exitosa.
    @PostMapping("")
    ResponseEntity<DeviceDto> postDevice(DeviceDto deviceDto){
        return ResponseEntity.ok(deviceService.postDevice(deviceDto));

    }

    //Obtener todos los dispositivos de un tipo específico
    //GET /device?type=Laptop
    @GetMapping("{type}")
    ResponseEntity<List<DeviceDto>> getDeviceByType(@RequestParam DeviceType type){
        return ResponseEntity.ok(deviceService.getDeviceByType(type));

    }

    // Obtener todos los dispositivos que tengan asociada la ultima telemetria registrada con un consumo de cpu entre
    //dos valores pasados por parametros. (No permitir que el lowThreshold sea mayor al upThreshold, en dicho caso devolver 400 bad request )
    //GET /device?lowThreshold=70&upThreshold=80

    @GetMapping("{upThreshold}/{lowThreshold}")
    ResponseEntity<DeviceDto> getDeviceByCpuUsageBetween(@RequestParam String upThreshold, @RequestParam String lowThreshold){
        return ResponseEntity.ok(deviceService.getDeviceByCpuUsageBetween(upThreshold, lowThreshold));
    }

}