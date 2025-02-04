package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDto;
import ar.edu.utn.frc.tup.lciii.model.Device;
import ar.edu.utn.frc.tup.lciii.model.DeviceType;
import ar.edu.utn.frc.tup.lciii.model.Telemetry;
import ar.edu.utn.frc.tup.lciii.repos.DeviceRepo;
import ar.edu.utn.frc.tup.lciii.repos.TelemetryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private TelemetryRepo telemetryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DeviceDto postDevice(DeviceDto deviceDto) {

        //POST /device
        //Recibe un JSON con los datos del dispositivo.
        Device device = modelMapper.map(deviceDto, Device.class);

        //Valida que el hostname no exista preeviamente en la tabla Device, de lo contrario, rechazar la solicitud.
        Device exists = deviceRepo.findByHostName(device.getHostName());

        if (exists.getHostName().equalsIgnoreCase(device.getHostName())){
            throw new IllegalArgumentException("hostname ya esta registrado");
        }
        //Guarda los datos en la base de datos.
        device.setCreatedDate(LocalDateTime.now());

        return modelMapper.map(deviceRepo.save(device), DeviceDto.class);
    }

    @Override
    public List<DeviceDto> getDeviceByType(DeviceType type) {
        List<Device> deviceDtoList = deviceRepo.findAllByType(type);
        List<DeviceDto> devices = new ArrayList<>();
        for(Device d : deviceDtoList) {
            DeviceDto dto = modelMapper.map(d, DeviceDto.class);
            devices.add(dto);
        }
        return devices;
    }

    @Override
    public DeviceDto getDeviceByCpuUsageBetween(String  upThreshold, String lowThreshold) {
        List<Telemetry> latestTelemetry = telemetryRepo.findTelemetryByCpuUsageBetween(upThreshold, lowThreshold);

        //Obtener todos los dispositivos que tengan asociada la ultima telemetria registrada con un consumo de cpu entre
        //dos valores pasados por parametros. (No permitir que el lowThreshold sea mayor al upThreshold, en dicho caso devolver 400 bad request )
        return modelMapper.map(latestTelemetry.get(0).getDevice(), DeviceDto.class);
    }

}
