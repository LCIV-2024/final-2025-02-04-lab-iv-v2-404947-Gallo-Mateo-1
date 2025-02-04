package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDto;
import ar.edu.utn.frc.tup.lciii.model.DeviceType;

import java.util.List;

public interface DeviceService {
    //POST /device
    //Recibe un JSON con los datos del dispositivo.
    //Valida que el hostname no exista preeviamente en la tabla Device, de lo contrario, rechazar la solicitud.
    //Guarda los datos en la base de datos.
    //Retorna un 201 Created si la operación fue exitosa.
    DeviceDto postDevice(DeviceDto deviceDto);

    //Obtener todos los dispositivos de un tipo específico
    //GET /device?type=Laptop
    List<DeviceDto> getDeviceByType(DeviceType type);

    // Obtener todos los dispositivos que tengan asociada la ultima telemetria registrada con un consumo de cpu entre
    //dos valores pasados por parametros. (No permitir que el lowThreshold sea mayor al upThreshold, en dicho caso devolver 400 bad request )
    //GET /device?lowThreshold=70&upThreshold=80

    DeviceDto getDeviceByCpuUsageBetween(String upThreshold, String lowThreshold);

}
