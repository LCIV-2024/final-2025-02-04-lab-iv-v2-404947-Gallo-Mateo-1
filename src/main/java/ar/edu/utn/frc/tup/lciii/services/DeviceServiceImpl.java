package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDto;
import ar.edu.utn.frc.tup.lciii.model.Device;
import ar.edu.utn.frc.tup.lciii.model.DeviceType;
import ar.edu.utn.frc.tup.lciii.model.Telemetry;
import ar.edu.utn.frc.tup.lciii.repos.DeviceRepo;
import ar.edu.utn.frc.tup.lciii.repos.TelemetryRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private TelemetryRepo telemetryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    //El endpoint POST /api/save-consumed-devices debe cumplir con los siguientes criterios:
    //Se debe insertar en la base de datos la mitad de los dispositivos devueltos por la API.
    //La selección de los dispositivos a insertar debe ser aleatoria, evitando siempre insertar los mismos.
    //El ID de los dispositivos no debe ser considerado al momento de la inserción.
    //El campo createdDate en la base de datos debe reflejar el momento exacto de la inserción, en lugar de la fecha
    // proporcionada por la API.
//    @Override
//    public List<DeviceDto> postRandomDevices(){
//        //7. Crear otro endpoint de tipo post en donde vaya a buscar dispositivos a la siguiente url:
//        //'https://67a106a15bcfff4fabe171b0.mockapi.io/api/v1/device/device'
//        String url = "";
//        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
//        if (response == null || response.isEmpty()) {
//            throw new RuntimeException("La API no devolvió dispositivos válidos");
//        }
//        //El ID de los dispositivos no debe ser considerado al momento de la inserción.
//        //El campo createdDate en la base de datos debe reflejar el momento exacto de la inserción, en lugar de la fecha
//        // proporcionada por la API.
//        List<Device> deviceListResponse = response.stream().map(this::mapJsonToDevice).collect(Collectors.toList());
//
//        //Se debe insertar en la base de datos la mitad de los dispositivos devueltos por la API.
//        //La selección de los dispositivos a insertar debe ser aleatoria, evitando siempre insertar los mismos.
//        Collections.shuffle(deviceListResponse);
//        int mitadLista = deviceListResponse.size()/2;
//        int index = 0;
//        List<Device> devicesToSave = new ArrayList<>();
//
//        while (mitadLista > 0 && index < deviceListResponse.size()) {
//            Device d = deviceListResponse.get(index);
//            Device exists = deviceRepo.findByHostName(d.getHostName());
//
//            if(exists == null){
//                devicesToSave.add(d);
//                mitadLista--;
//            }
//            index++;
//        }
//
//        List<Device> savedDevices = deviceRepo.saveAll(devicesToSave);
//
//        return savedDevices.stream().map(this::mapToDeviceDto).collect(Collectors.toList());
//    }
    @Override
    public List<DeviceDto> postRandomDevices(){
        String url = "https://679d36d887618946e654a137.mockapi.io/final-lciv/Device";
        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

        if (response == null || response.isEmpty()) {
            throw new RuntimeException("La API no devolvió dispositivos válidos");
        }

        //El ID de los dispositivos no debe ser considerado al momento de la inserción.
        //El campo createdDate en la base de datos debe reflejar el momento exacto de la inserción, en lugar de la fecha
        // proporcionada por la API.
        List<Device> deviceListResponse = response.stream().map(this::mapJsonToDevice).collect(Collectors.toList());

        //La selección de los dispositivos a insertar debe ser aleatoria, evitando siempre insertar los mismos.
        Collections.shuffle(deviceListResponse);

        //Se debe insertar en la base de datos la mitad de los dispositivos devueltos por la API.
        int cantidadAInsertar = deviceListResponse.size() / 2;
        List<Device> devicesToSave = new ArrayList<>();

        for (Device d : deviceListResponse) {
            if (devicesToSave.size() == cantidadAInsertar) {
                break; // tenemos la cantidad necesaria (la mitad de los devices devueltos)
            }

            if (deviceRepo.findByHostName(d.getHostName()) == null) {
                devicesToSave.add(d);
            }
        }

        List<Device> savedDevices = deviceRepo.saveAll(devicesToSave);
        return savedDevices.stream().map(this::mapToDeviceDto).collect(Collectors.toList());
    }


    private Device mapJsonToDevice(Map<String, Object> data) {
        //El ID de los dispositivos no debe ser considerado al momento de la inserción.
        //El campo createdDate en la base de datos debe reflejar el momento exacto de la inserción, en lugar de la fecha
        // proporcionada por la API.
        return Device.builder()
                .id(null)
                .hostName((String) data.get("hostName"))
                .createdDate(LocalDateTime.now())
                .telemetry(mapJsonToTelemetry((Map<String, Object>) data.get("telemetry")))
                .type(DeviceType.valueOf((String) data.get("type")))
                .os((String) data.get("os"))
                .ip((String) data.get("ip"))
                .macAddress((String) data.get("macAddress"))
                .build();
    }


    private Telemetry mapJsonToTelemetry(Map<String, Object> data) {
        Object dateObj = data.get("dataDate");
        LocalDateTime parsedDate;

        if (dateObj instanceof LocalDateTime) {
            parsedDate = (LocalDateTime) dateObj;
        } else if (dateObj instanceof String) {
            parsedDate = LocalDateTime.parse((String) dateObj, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else {
            throw new IllegalArgumentException("Formato de fecha inválido para dataDate: " + dateObj);
        }

        return Telemetry.builder()
                .id(((Number) data.get("id")).longValue())
                .hostname((String) data.get("hostname"))
                .ip((String) data.get("ip"))
                .dataDate(parsedDate)
                .hostDiskFree(((Number) data.get("hostDiskFree")).doubleValue())
                .cpuUsage(((Number) data.get("cpuUsage")).doubleValue())
                .microphoneState((String) data.get("microphoneState"))
                .screenCaptureAllowed((Boolean) data.get("screenCaptureAllowed"))
                .audioCaptureAllowed((Boolean) data.get("audioCaptureAllowed"))
                .build();
    }



    @Override
    public DeviceDto postDevice(DeviceDto deviceDto) {
        //POST /device
        //Recibe un JSON con los datos del dispositivo.
        Device device = mapToDevice(deviceDto);

        //Valida que el hostname no exista preeviamente en la tabla Device, de lo contrario, rechazar la solicitud.
        Device exists = deviceRepo.findByHostName(deviceDto.getHostName());

        if (exists != null){
            throw new IllegalArgumentException("hostname ya esta registrado");
        }
        //Guarda los datos en la base de datos.
        device.setCreatedDate(LocalDateTime.now());

        deviceRepo.save(device);

        return mapToDeviceDto(device);
    }

    @Override
    public List<DeviceDto> getDeviceByType(DeviceType type) {
        List<Device> deviceDtoList = deviceRepo.findAllByType(type);
        List<DeviceDto> devices = new ArrayList<>();
        for(Device d : deviceDtoList) {
            DeviceDto dto = mapToDeviceDto(d);
            devices.add(dto);
        }
        return devices;
    }

    @Override
    public DeviceDto getDeviceByCpuUsageBetween(double  upThreshold, double lowThreshold) {

        List<Telemetry> latestTelemetry = telemetryRepo.findTelemetryByCpuUsageBetween(upThreshold, lowThreshold);

        //Obtener todos los dispositivos que tengan asociada la ultima telemetria registrada con un consumo de cpu entre
        //dos valores pasados por parametros. (No permitir que el lowThreshold sea mayor al upThreshold, en dicho caso devolver 400 bad request )
        Device deviceFounded = latestTelemetry.get(0).getDevice();

        if (deviceFounded != null){
            return mapToDeviceDto(deviceFounded);
        } else {
            throw new IllegalArgumentException("no se encontro telemetry con cpuUsage entre " + lowThreshold + " y " + upThreshold);
        }
    }

    private Device mapToDevice(DeviceDto dto){
        Device device = new Device();
        device.setHostName(dto.getHostName());
        device.setType(dto.getType());
        device.setOs(dto.getOs());
        device.setIp(dto.getIp());
        device.setMacAddress(dto.getMacAddress());

        return device;
    }

    private DeviceDto mapToDeviceDto(Device device){
        DeviceDto dto = new DeviceDto();
        dto.setHostName(device.getHostName());
        dto.setType(device.getType());
        dto.setOs(device.getOs());
        dto.setIp(device.getIp());
        dto.setMacAddress(device.getMacAddress());

        return dto;
    }


}
